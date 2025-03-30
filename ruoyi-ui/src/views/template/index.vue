<template>
  <div>
    <CanvasEditor
      ref="canvasEditor"
      :parentContent="parentContent"
      @save-content="handleSaveCanvasEditorContent"
    />

    <button
      style="
        width: 160px;
        height: 80px;
        border: 2px solid #2b4b6b;
        margin-right: 20px;
        float: right;
      "
      @click="handleSaveContent"
    >
      保 存
    </button>
  </div>
</template>

<script>
import CanvasEditor from "@/components/CanvasEditor/index.vue";
export default {
  name: "ParentComponent",
  components: {
    CanvasEditor,
  },
  data() {
    return {
      parentContent: undefined, // 存放父组件传递的数据
      content: undefined, // 存放子组件数据
    };
  },
  mounted() {
    console.log("模拟父组件向后端请求数据, 传递给子组件");
    this.parentContent = {
      header: [
        {
          value: "父类传递的数据",
          size: 12,
          bold: false,
          color: "rgb(33, 53, 71)",
          italic: false,
        },
      ],
      main: [
        {
          value: "父类传递的数据 通过后端获取",
          size: 40,
          bold: true,
        },
      ],
    };
  },
  methods: {
    handleSaveContent() {
      console.log(222);
      console.log(
        "父组件保存数据时即触发点击事件,执行 saveContent 方法获取子组件的数据"
      );
      this.$refs.canvasEditor.saveContent();
      // 将获取到的子组件数据 this.content 入库处理
    },

    handleSaveCanvasEditorContent(data) {
      console.log("从子组件接收到的数据:", data);
      // 将data数据转换为 json 格式的数据, 方便入库处理
      console.log(111);

      this.content = JSON.stringify(data);
      console.log("转换后的数据 this.content 为: ", this.content);
    },
  },
};
</script>
