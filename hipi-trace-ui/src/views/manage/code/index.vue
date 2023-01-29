<template>
  <div class='page'>
    <page-header-wrapper>
      <a-card :bordered='false'>
      </a-card>
      <a-card :bordered='false'>
        <a-row :gutter='24'>
          <a-col :span='24'>
            <!-- 条件搜索 -->
            <div class='table-page-search-wrapper'>
              <a-form layout='inline'>
                <a-row :gutter='48'>
                  <a-col :md='8' :sm='24'>
                    <a-form-item label='批次号'>
                      <a-input v-model='queryParam.batchCode' placeholder='请输入' allow-clear />
                    </a-form-item>
                  </a-col>
                  <a-col :md='8' :sm='24'>
                    <a-form-item label='品类名称'>
                      <a-input v-model='queryParam.categoryName' placeholder='请输入' allow-clear />
                    </a-form-item>
                  </a-col>
                  <a-col :md='!advanced && 8 || 24' :sm='24'>
                    <template class='table-page-search-submitButtons'
                              :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                      <a-button type='primary' @click='handleQuery'>
                        <a-icon type='search' />
                        查询
                      </a-button>
                      <a-button style='margin-left: 8px' @click='resetQuery'>
                        <a-icon type='redo' />
                        重置
                      </a-button>
                    </template>
                  </a-col>
                </a-row>
              </a-form>
            </div>

            <div class='table-operations'>
              <a-button type='primary' @click='handleAdd()' v-hasPermi="['system:user:add']">
                <a-icon type='plus' />
                新增
              </a-button>
              <table-setting
                :style="{float: 'right'}"
                :table-size.sync='tableSize'
                v-model='columns'
                :refresh-loading='loading'
                @refresh='getList' />
            </div>
            <!-- 数据展示 -->
            <a-table
              :loading='loading'
              :size='tableSize'
              rowKey='userId'
              :columns='columns'
              :data-source='list'
              :pagination='false'
              :bordered='tableBordered'>

              <span slot='codeType' slot-scope='text, record'>
                <div v-if="record.charType == '1'" >明码</div>
                <div v-if="record.charType == '2'" >暗码</div>
              </span>

              <span slot='charType' slot-scope='text, record'>
                <div v-if="record.charType == '1'" >纯数字</div>
                <div v-if="record.charType == '2'" >纯字母</div>
                <div v-if="record.charType == '3'" >混合类型</div>
              </span>

            </a-table>
            <!-- 分页 -->
            <a-pagination
              class='ant-table-pagination'
              show-size-changer
              show-quick-jumper
              :current='queryParam.current'
              :total='total'
              :page-size='queryParam.size'
              :showTotal='total => `共 ${total} 条`'
              @showSizeChange='onShowSizeChange'
              @change='changeSize'
            />
          </a-col>
        </a-row>
      </a-card>

      <a-modal
        :title='title'
        :visible='open'
        :confirm-loading='submitLoading'
        @ok='submitForm'
        @cancel='cancel'
        :width='700'
      >

        <a-form-model ref='form' :model='form' :rules='rules'>
          <a-form-model-item label='产品名称' prop='categoryManageId'>
            <a-select @change='changge($event)' v-model='form.categoryManageId' placeholder='请选择产品名称'
                      style='width: 300px;'>
              <a-select-option :value='item.id' v-for='(item,index) in goodsData'>
                {{ item.categoryName }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item label='生产批次号' prop='batchCode'>
            <a-select v-model='form.batchCode' placeholder='请选择生产批次号' style='width: 300px;'>
              <a-select-option :value='item.batchCode' v-for='(item,index) in categoryBatch'>
                {{ item.batchCode }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item label='码类型' prop='codeType'>
            <a-radio-group v-model='form.codeType'>
              <a-radio value='1'>
                明码
              </a-radio>
              <a-radio value='2'>
                暗码
              </a-radio>
            </a-radio-group>
          </a-form-model-item>
          <a-form-model-item label='验证码' prop='captcha' v-if='form.codeType==2'>
            <a-input v-model='form.captcha' placeholder='请输入验证码' allow-clear />
          </a-form-model-item>

          <a-form-model-item label='生码规则' prop='genCodeRule'>
            <a-select v-model='form.genCodeRule' placeholder='' style='width: 300px;'>
              <a-select-option value='1'>
                纯数字
              </a-select-option>
              <a-select-option value='2'>
                纯字母
              </a-select-option>
              <a-select-option value='3'>
                混合类型
              </a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item label='字符长度' prop='charLength'>
            <a-input-number size='large' :min='18' :max='32' :default-value='3' v-model='form.charLength' />
            单位
          </a-form-model-item>
          <a-form-model-item label='生码数量' prop='genNumber'>
            <a-input-number size='large' :min='1' :max='100000' :default-value='3' v-model='form.genNumber' />
          </a-form-model-item>
        </a-form-model>
      </a-modal>
    </page-header-wrapper>
  </div>
</template>

<script>

import { getCategoryBatch, listCode } from '@/api/code/index'
import { genCode } from '@/api/batch/index'
import { selectList } from '@/api/category/index'
import { tableMixin } from '@/store/table-mixin'
import { PlusOutlined } from '@ant-design/icons-vue'

export default {
  name: 'batch',
  components: {
    PlusOutlined
  },
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      total: 0,
      // 树选项
      deptOptions: [{
        id: 0,
        label: '',
        children: []
      }],
      defaultSelectedKeys: [],
      // 日期范围
      dateRange: [],
      queryParam: {
        current: 1,
        size: 10,
        code: undefined,
        name: undefined
      },
      replaceFields: { children: 'children', title: 'name', key: 'id', value: 'id' },
      columns: [
        {
          title: '品类名称',
          dataIndex: 'categoryName',
          align: 'center'
        },
        {
          title: '批次号',
          dataIndex: 'batchCode',
          align: 'center'
        },
        {
          title: '码类型',
          dataIndex: 'codeType',
          scopedSlots: { customRender: 'codeType' },
          align: 'center'
        },
        {
          title: '字符长度',
          dataIndex: 'charLength',
          align: 'center'
        },
        {
          title: '字符类型',
          dataIndex: 'charType',
          scopedSlots: { customRender: 'charType' },
          align: 'center'
        },
        {
          title: '生成数量',
          dataIndex: 'genNumber',
          align: 'center'
        }
      ],
      visible: true,
      form: {},
      open: false,
      submitLoading: false,
      title: '新增二维码',
      node: '',
      rules: {
        categoryManageId: [
          { required: true, message: '请选择商品', trigger: 'change' }
        ],
        batchCode: [
          { required: true, message: '请填写批次号', trigger: 'blur' }
        ],
        codeType: [
          { required: true, message: '请选择码类型', trigger: 'change' }
        ],
        genNumber: [
          { required: true, message: '请填写生码数量', trigger: 'blur' }
        ],
        genCodeRule: [
          { required: true, message: '请选择生码规则', trigger: 'change' }
        ],
        captcha: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ]
      },
      goodsData: [],
      categoryBatch: []


    }
  },
  filters: {},
  created() {
    this.getList()
  },
  computed: {},
  watch: {},
  methods: {
    changge(event) {
      console.log(event)
      var param = { 'categoryManageId': event }
      getCategoryBatch(param).then(response => {
          this.categoryBatch = response.data
        }
      )
    },
    /** 查询列表 */
    getGoodsList() {
      selectList().then(response => {
          this.goodsData = response.data
        }
      )
    },
    getCategoryBatch() {
      getCategoryBatch().then(response => {
          this.categoryBatch = response.data
        }
      )
    },

    /** 查询列表 */
    getList() {
      this.loading = true
      listCode(this.queryParam).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.loading = false
        }
      )
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.current = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.queryParam = {
        current: 1,
        size: 10,
        code: undefined,
        name: undefined
      }
      this.handleQuery()
    },
    onShowSizeChange(current, pageSize) {
      this.queryParam.size = pageSize
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.current = current
      this.queryParam.size = pageSize
      this.getList()
    },
    handleAdd() {
      this.title = '新增二维码'
      this.open = true
      this.getGoodsList()
      this.getCategoryBatch()
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.submitLoading = false
      this.reset()

    },
    reset() {
      this.form = {}
    },


    /** 创建和修改 */
    submitForm: function() {

      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          let data = JSON.parse(JSON.stringify(this.form))
          if (this.title == '新增二维码') {
            genCode(data).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.cancel()
              this.getList()
            }).finally(() => {
              this.submitLoading = false
            })
          }

        } else {
          return false
        }
      })
    }
  }
}
</script>
<style lang='less' scoped>
.toRequired {
  color: #ff4949;
  padding-right: 2px;
}

.title {
  font-weight: bold;

}

:deep(.ant-form-item) {
  display: flex !important;

}

.green, .red {
  position: relative;
}

.green::before {
  content: "";
  position: absolute;
  background: green;
  color: green;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  top: 8px;
  left: 35px;

}

.red::before {
  content: "";
  position: absolute;
  background: red;
  color: red;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  top: 8px;
  left: 35px;

}
</style>
