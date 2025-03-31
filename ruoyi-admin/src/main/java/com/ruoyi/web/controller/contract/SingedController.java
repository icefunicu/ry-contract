package com.ruoyi.web.controller.contract;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.service.IContractService;
import com.ruoyi.web.service.IContractSignerService;
import com.ruoyi.web.service.impl.EntSealClipService;
import io.swagger.annotations.ApiOperation;
import org.resrun.sdk.service.CalculatePositionService;
import org.resrun.sdk.service.EntSealGenerateService;
import org.resrun.sdk.service.SDKService;
import org.resrun.sdk.service.pojo.RealPositionProperty;
import org.resrun.sdk.utils.Base64;
import org.resrun.sdk.vo.base.SignLocation;
import org.resrun.sdk.vo.request.CertEventRequest;
import org.resrun.sdk.vo.request.DocumentSignRequest;
import org.resrun.sdk.vo.response.CertEventResponse;
import org.resrun.sdk.vo.response.DocumentSignResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/openSign")
public class SingedController  extends BaseController {

    @Autowired
    private EntSealClipService entSealClipService ;
    @Autowired
    private EntSealGenerateService entSealGenerateService ;
    @Autowired
    private SDKService sdkService;
    @Autowired
    private CalculatePositionService calculatePositionService ;
    @Autowired
    private IContractService contractService;
    @Autowired
    private IContractSignerService contractSignerService;

    @ApiOperation("生成企业签章-上传生成")
    @RequestMapping(value = "/clip/seal",method = RequestMethod.POST)
    public Result<SealResponse> generateUpload(@RequestBody ClipSealRequest request){

        if(request.getImage() == null || request.getImage().length() == 0){
            return Result.error("图片数据为空",null);
        }
        byte[] decode = Base64.decode(request.getImage());
        if(decode == null || decode.length == 0){
            return Result.error("签章制作失败",null);
        }

        byte[] entSealByte = entSealClipService.clip(decode, request.getColorRange());
        if(entSealByte == null){
            return Result.error("签章制作失败",null);
        }
        String encode = Base64.encode(entSealByte);
        SealResponse response = new SealResponse();
        response.setEntSeal(encode);
        return Result.OK(response) ;

    }
    @ApiOperation("生成企业签章-参数生成")
    @RequestMapping(value = "/generate/seal",method = RequestMethod.POST)
    public Result<SealResponse> seal(@RequestBody GenerateSealRequest request){


        if(request == null || request.getMiddleText() == null || request.getTopText() == null){
            return Result.error("参数缺失",null) ;
        }
        byte[] entSealByte = entSealGenerateService.generateEntSeal(request.getTopText(), request.getMiddleText());
        if(entSealByte == null){
            return Result.error("签章制作失败",null);
        }
        String encode = Base64.encode(entSealByte);
        SealResponse response = new SealResponse();
        response.setEntSeal(encode);
        return Result.OK(response) ;

    }
    @ApiOperation("签署")
    @RequestMapping(value = "/sign",method = RequestMethod.POST)
    public Result<SignResponse> sign(@RequestBody SignRequest request){
        int contractId = request.getContractId();
        Long userId = getUserId();

        Contract contract = contractService.selectContractById((long) contractId);
        if(contract == null){
            return Result.error("合同不存在",null);
        }
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource1 = classLoader.getResource("");
        String classpath = resource1.getPath();
        if (classpath.startsWith("/")) {
            classpath = classpath.substring(1);
        }

        String fileName = classpath + "static/contract_" + contractId + ".pdf";

        byte[] signFileBytes = null ;
        byte[] entSealBytes = null ;
        byte[] personalBytes = null ;
        org.resrun.sdk.vo.base.Result<CertEventResponse> entCert = null ;
        org.resrun.sdk.vo.base.Result<CertEventResponse> personalCert = null ;
        List<RealPositionProperty> entPositionList = null;
        List<RealPositionProperty> personalPositionList = null;
        int entSealWidth = 200 ;
        int entSealHeight = 200 ;
        int personalSealWidth = 150 ;
        int personalSealHeight = 70 ;
        //获取本地签署文件
        try {
            signFileBytes = getResourceFiles(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(signFileBytes == null){
            return Result.error("签署失败",null);
        }
        //生成企业证书和个人证书
        try {
            if(request.getEntName() != null && request.getEntName().length() > 0){
                CertEventRequest certEventRequest = new CertEventRequest();
                certEventRequest.setCertSubject("开放签@" + request.getEntName());
                certEventRequest.setCertPassword("123456");
                certEventRequest.setUniqueCode(UUID.randomUUID().toString());
                entCert =  sdkService.certEvent(certEventRequest);
                if(entCert == null || !entCert.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    return Result.error(entCert.getMessage(),null);
                }
            }
            if(request.getPersonalName() != null && request.getPersonalName().length() > 0){
                CertEventRequest certEventRequest = new CertEventRequest();
                certEventRequest.setCertSubject("开放签@" + request.getPersonalName());
                certEventRequest.setCertPassword("123456");
                certEventRequest.setUniqueCode(UUID.randomUUID().toString());
                personalCert =  sdkService.certEvent(certEventRequest);
                if(personalCert == null || !personalCert.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    return Result.error(personalCert.getMessage(),null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //生成企业签章和个人签章
        if(request.getEntSeal() != null){
            entSealBytes = Base64.decode(request.getEntSeal());
        }
        if(request.getPersonalSeal() != null){
            personalBytes = Base64.decode(request.getPersonalSeal());
        }
        //进行签署操作
        byte[] operationByte = signFileBytes ;

        //计算企业签署位置和个人签署位置
        if(SignTypeEnum.POSITION.getCode().equals(request.getSignType())){
            DocumentSignRequest signRequest = new DocumentSignRequest();
            signRequest.setUniqueCode(UUID.randomUUID().toString());
            signRequest.setSignType(SDKSignTypeEnum.POSITION.getCode());
            // 允许单方完成签署，不再要求双方都必须签署
            if((request.getEntPositionList() == null || request.getEntPositionList().size() == 0 ) &&
                    (request.getPersonalPositionList() == null || request.getPersonalPositionList().size() == 0)){
                return Result.error("签署失败，至少需要一方进行签署",null);
            }
            //计算企业签署位置
            if(request.getEntPositionList() != null && request.getEntPositionList().size() > 0){
                signRequest.setCertPassword(entCert.getData().getCertPassword());
                signRequest.setPfx(entCert.getData().getPfx());
                signRequest.setSignatureFile(Base64.encode(entSealBytes));
                signRequest.setDocumentFile(Base64.encode(operationByte));

                List<SignLocation> signLocations = new ArrayList<>();
                for(PositionRequest positionRequest : request.getEntPositionList()){
                    SignLocation signLocation = new SignLocation();
                    signLocation.setOffsetX(Float.valueOf(positionRequest.getOffsetX()));
                    signLocation.setOffsetY(Float.valueOf(positionRequest.getOffsetY()));
                    signLocation.setPage(positionRequest.getPage());
                    signLocation.setPageHeight(Float.valueOf(positionRequest.getPageHeight()));
                    signLocation.setPageWidth(Float.valueOf(positionRequest.getPageWidth()));
                    signLocation.setSignHeight(Float.valueOf(positionRequest.getHeight()));
                    signLocation.setSignWidth(Float.valueOf(positionRequest.getWidth()));
                    signLocations.add(signLocation);
                }
                signRequest.setSignLocationList(signLocations);
                org.resrun.sdk.vo.base.Result<DocumentSignResponse> signResponse = sdkService.documentSign(signRequest);
                if(signResponse.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    operationByte = signResponse.getData().getDocumentFile();
                }else {
                    return Result.error("签署失败",null);
                }

            }
            //计算个人签署位置
            if(request.getPersonalPositionList() != null && request.getPersonalPositionList().size() > 0){

                signRequest.setCertPassword(personalCert.getData().getCertPassword());
                signRequest.setPfx(personalCert.getData().getPfx());
                signRequest.setSignatureFile(Base64.encode(personalBytes));
                signRequest.setDocumentFile(Base64.encode(operationByte));

                List<SignLocation> signLocations = new ArrayList<>();
                for(PositionRequest positionRequest : request.getPersonalPositionList()){
                    SignLocation signLocation = new SignLocation();
                    signLocation.setOffsetX(Float.valueOf(positionRequest.getOffsetX()));
                    signLocation.setOffsetY(Float.valueOf(positionRequest.getOffsetY()));
                    signLocation.setPage(positionRequest.getPage());
                    signLocation.setPageHeight(Float.valueOf(positionRequest.getPageHeight()));
                    signLocation.setPageWidth(Float.valueOf(positionRequest.getPageWidth()));
                    signLocation.setSignHeight(Float.valueOf(positionRequest.getHeight()));
                    signLocation.setSignWidth(Float.valueOf(positionRequest.getWidth()));
                    signLocations.add(signLocation);
                }
                signRequest.setSignLocationList(signLocations);
                org.resrun.sdk.vo.base.Result<DocumentSignResponse> signResponse = sdkService.documentSign(signRequest);
                if(signResponse.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    operationByte = signResponse.getData().getDocumentFile();

                }else {
                    return Result.error("签署失败",null);
                }
            }

        }else if(SignTypeEnum.KEYWORDS.getCode().equals(request.getSignType())){
            // 允许单方完成签署，不再要求双方都必须签署
            if((request.getEntKeyword() == null || request.getEntKeyword().length() == 0 ) &&
                    (request.getPersonalKeyword() == null || request.getPersonalKeyword().length() == 0)){
                return Result.error("签署失败，至少需要一方设置关键字进行签署",null);
            }
            DocumentSignRequest signRequest = new DocumentSignRequest();
            signRequest.setUniqueCode(UUID.randomUUID().toString());
            signRequest.setSignType(SDKSignTypeEnum.KEYWORDS.getCode());
            //根据关键字计算所有企业签署位置
            if(request.getEntKeyword() != null && request.getEntKeyword().length() > 0){
                entPositionList = calculatePositionService.getAllPositionByKeyWords(signFileBytes, request.getEntKeyword(), entSealWidth, entSealHeight);
                signRequest.setCertPassword(entCert.getData().getCertPassword());
                signRequest.setPfx(entCert.getData().getPfx());
                signRequest.setSignatureFile(Base64.encode(entSealBytes));
                signRequest.setDocumentFile(Base64.encode(operationByte));
                signRequest.setKeywords(request.getEntKeyword());
                org.resrun.sdk.vo.base.Result<DocumentSignResponse> signResponse = sdkService.documentSign(signRequest);
                if(signResponse.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    operationByte = signResponse.getData().getDocumentFile();
                }else {
                    return Result.error("签署失败",null);
                }
            }
            //根据关键字计算所有个人签署位置
            if(request.getPersonalKeyword() != null && request.getPersonalKeyword().length() > 0){
                personalPositionList = calculatePositionService.getAllPositionByKeyWords(signFileBytes,request.getPersonalKeyword(),personalSealWidth,personalSealHeight);
                signRequest.setCertPassword(personalCert.getData().getCertPassword());
                signRequest.setPfx(personalCert.getData().getPfx());
                signRequest.setSignatureFile(Base64.encode(personalBytes));
                signRequest.setDocumentFile(Base64.encode(operationByte));
                signRequest.setKeywords(request.getPersonalKeyword());
                org.resrun.sdk.vo.base.Result<DocumentSignResponse> signResponse = sdkService.documentSign(signRequest);
                if(signResponse.getCode().equals(APIResultEnum.SUCCESS.getCode())){
                    operationByte = signResponse.getData().getDocumentFile();
                }else {
                    return Result.error("签署失败",null);
                }
            }

            if((personalPositionList == null || personalPositionList.size() == 0 ) &&
                    (entPositionList == null || entPositionList.size() == 0)){
                return Result.error("签署失败！签署关键字在文件中不存在，请准确设置关键字后再签署",null);
            }
        }

        // 将签署后的文件写回原始文件路径，覆盖原文件
        try {
            // 获取原始文件的完整路径
            java.io.File file = new java.io.File(fileName);
            // 确保目录存在
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 写入文件
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            fos.write(operationByte);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            // 即使写入失败，也继续返回签署结果
        }

        ContractSigner contractSigner = contractSignerService.selectContractSignerByContractIdAndUserId((long) contractId, userId);

        contractSigner.setSigned(1);
        contractSigner.setSignImage("http://localhost:8080/contract_" + contractId + ".pdf");
        contractSignerService.updateContractSigner(contractSigner);

        // 判断是否双方都已签署，如果是，则将合同状态改为已签署
        ContractSigner contractSigner1 = new ContractSigner();
        contractSigner1.setContractId((long) contractId);
        List<ContractSigner> contractSigners = contractSignerService.selectContractSignerList(contractSigner1);
        boolean allSigned = true;
        for (ContractSigner signer : contractSigners) {
            if (signer.getSigned() == 0) {
                allSigned = false;
                break;
            }
        }
        if (allSigned) {
            contract.setStatus("已签署");
            contractService.updateContract(contract);
        }

        String encode = Base64.encode(operationByte);
        SignResponse response = new SignResponse();
        response.setSignFile(encode);

        return Result.OK(response);

    }
    public byte[] getResourceFiles(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("文件不存在: " + path);
                return null;
            }
            FileInputStream inputStream = new FileInputStream(file);
            return read(inputStream);
        } catch (Exception e) {
            System.err.println("读取文件失败: " + path);
            e.printStackTrace();
        }
        return null;
    }


    public byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
