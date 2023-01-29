<template>
  <div>
    <Tinymce v-model='defaultValue' :height='300' placeholder='在这里输入文字' />
  </div>
</template>

<script>
import Tinymce from '../index.vue'

export default {
  components: {
    Tinymce
  },
  props: {},
  data() {
    return {
      defaultValue: '<p>配置文档参阅：http://tinymce.ax-z.cn</p>'
    }
  },
  computed: {},
  watch: {
    defaultValue(newv) {
      console.log('富文本值改变:', newv)
    }
  },
  created() {

  },
  mounted() {

  },
  methods: {}
}
</script>
