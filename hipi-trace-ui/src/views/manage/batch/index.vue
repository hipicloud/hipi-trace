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
                      <a-input v-model='queryParam.categoryManageName' placeholder='请输入' allow-clear />
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
              <template slot='status' slot-scope='text, record'>

                <div v-if="record.status == '1'" class='statusDiv'><span class='green'></span>已激活</div>
                <div v-if="record.status == '2'" class='statusDiv'><span class='red'></span>已冻结</div>

              </template>
              <template slot='operation' slot-scope='text, record'>
                <a @click='see(record)'>
                  <a-icon type='eye' />
                  预览
                </a>
                <a-divider type='vertical' />
                <a @click='changeStatus(record)'>
                  <a-icon type='form' />
                  <span v-if="record.status == '1'">冻结</span>
                  <span v-if="record.status == '2'">激活</span>
                </a>

              </template>
              <template slot='link' slot-scope='text, record'>
                <a @click='setting(record)'>
                  <a-icon type='edit' />
                  配置
                </a>
              </template>
              <template slot='QRcode' slot-scope='text, record'>
                <a @click='handleUpdate(record)'>
                  <a-icon type='qrcode' />
                  生码
                </a>
                <a-divider type='vertical' v-if='record.genCodeNumber>0' />
                <a @click='downLoad(record)' v-if='record.genCodeNumber>0'>
                  <a-icon type='download' />
                  <span>下载</span>
                </a>

              </template>
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

        <a-form-model ref='form' :model='form' :rules='rules' v-if="title!='配置'">
          <a-form-model-item label='产品名称' prop='categoryManageId'>
            <a-select v-model='form.categoryManageId' placeholder='请选择产品名称' style='width: 300px;'>
              <a-select-option :value='item.id' v-for='(item,index) in goodsData'>
                {{ item.categoryName }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item label='生产批次号' prop='batchCode'>
            <a-input v-model='form.batchCode' placeholder='请输入生产批次号' allow-clear />
          </a-form-model-item>
          <a-form-model-item label='码类型' prop='codeType' v-if="title=='生码'">
            <a-radio-group v-model='form.codeType'>
              <a-radio value='1'>
                明码
              </a-radio>
              <a-radio value='2'>
                暗码
              </a-radio>
            </a-radio-group>
          </a-form-model-item>
          <a-form-model-item label='验证码' prop='captcha' v-if="title=='生码'&&form.codeType==2">
            <a-input v-model='form.captcha' placeholder='请输入验证码' allow-clear />
          </a-form-model-item>

          <a-form-model-item label='生码规则' prop='genCodeRule' v-if="title=='生码'">
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
          <a-form-model-item label='字符长度' prop='charLength' v-if="title=='生码'">
            <a-input-number size='large' :min='18' :max='32' :default-value='3' v-model='form.charLength' />
            单位
          </a-form-model-item>
          <a-form-model-item label='生码数量' prop='genNumber' v-if="title=='生码'">
            <a-input-number size='large' :min='1' :max='100000' :default-value='3' v-model='form.genNumber' />
          </a-form-model-item>


        </a-form-model>
        <div v-else>
          <a-form-model ref='form' :model='form'>
            <div v-for='(item,index1) in form.configList '>
              <label class='hjtitle'>环节{{ index1 + 1 }}:{{ item.traceabilityManageName }}</label>
              <a-table
                :columns='fieldColumns'
                :dataSource='item.catalogueFormList'
                :pagination='false'

              >
                <template slot='FiledTitle' slot-scope='text, record,index'>
                  {{ record.FiledTitle }}
                </template>
                <template slot='FiledType' slot-scope='text, record,index'>
                  <div v-if='record.FiledType==1'>文本</div>
                  <div v-if='record.FiledType==2'>多行文本</div>
                  <div v-if='record.FiledType==3'>图片</div>
                  <div v-if='record.FiledType==4'>视频</div>
                </template>
                <template slot='operation' slot-scope='text, record,index'>
                  <a @click='FiledDelete(index1,index)'>
                    删除
                  </a>
                </template>
              </a-table>
            </div>
          </a-form-model>
          <a-button block @click='chooseTrace'>
            选择环节
          </a-button>
        </div>


      </a-modal>
      <a-modal title='选择环节'
               :visible='traceOpen'
               :confirm-loading='traceSubmitLoading'
               @ok='traceSubmitForm'
               @cancel='traceCancel'
               :width='1000'>
        <!-- 数据展示 -->
        <div style='padding-bottom:30px'>
          <a-table
            :loading='traceLoading'
            rowKey='userId'
            :columns='traceColumns'
            :data-source='traceList'
            :pagination='false'
            :row-selection='rowSelection'
          >
            <span slot='status' slot-scope='text, record'>
                <div v-if="record.status == '0'" class='green'>启用</div>
                <div v-else class='red'>关闭</div>
            </span>

          </a-table>
          <!-- 分页 -->
          <a-pagination
            class='ant-table-pagination'
            show-size-changer
            show-quick-jumper
            :current='traceQueryParam.current'
            :total='traceTotal'
            :page-size='traceQueryParam.size'
            :showTotal='traceTotal => `共 ${traceTotal} 条`'
            @showSizeChange='traceOnShowSizeChange'
            @change='traceChangeSize'
          />
        </div>

      </a-modal>

      <a-modal title='预览'
               :visible='seeOpen'
               :width='500'
               class='yl'
               @cancel='ylCancel'

      >
        <template slot='footer'>
          <a></a>
        </template>

        <iframe id='iframe' :src='iframeSrc' style='height: calc(100% - 50px)' width='100%' frameborder='0'></iframe>
      </a-modal>


    </page-header-wrapper>

  </div>


</template>

<script>

import { addBatch, configure, downloadCode, genCode, getById, listBatch, updateStatus } from '@/api/batch/index'
import { listTraceability } from '@/api/traceability/index'
import { getCatalogueByDataId } from '@/api/form/index'
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
      traceList: [],
      selectedRowKeys: [],
      selectedRows: [],
      traceTableSize: 20,
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      traceLoading: false,
      total: 0,
      traceTotal: 0,
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
      traceQueryParam: {
        current: 1,
        size: 10
      },
      replaceFields: { children: 'children', title: 'name', key: 'id', value: 'id' },
      traceColumns: [
        {
          title: '溯源节点目录编号',
          dataIndex: 'traceabilityCatalogueCode',
          align: 'center'
        },
        {
          title: '溯源节点目录',
          dataIndex: 'traceabilityCatalogueName',
          align: 'center'
        },
        {
          title: '溯源节点编码',
          dataIndex: 'traceabilityCode',
          align: 'center'
        },
        {
          title: '溯源节点名称',
          dataIndex: 'traceabilityName',
          align: 'center'
        },

        {
          title: '创建人',
          dataIndex: 'createBy',
          align: 'center'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          align: 'center'
        }
      ],
      columns: [
        {
          title: '编号',
          dataIndex: 'key',
          key: 'key',
          align: 'center',
          customRender: (text, record, index) => `${index + 1}`//此处为重点

        },
        {
          title: '批次号',
          dataIndex: 'batchCode',
          align: 'center'
        },
        {
          title: '品类名称',
          dataIndex: 'categoryManageName',
          align: 'center'
        },
        {
          title: '溯源环节信息',
          dataIndex: 'link',
          scopedSlots: { customRender: 'link' },
          align: 'center'
        },
        {
          title: '二维码',
          dataIndex: 'QRcode',
          scopedSlots: { customRender: 'QRcode' },
          align: 'center'
        },
        {
          title: '二维码数量',
          dataIndex: 'genCodeNumber',
          align: 'center'
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ],
      visible: true,
      form: {},
      open: false,
      traceOpen: false,
      submitLoading: false,
      traceSubmitLoading: false,
      title: '新增批次',
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
        ],
        charLength: [
          { required: true, message: '请填写生码数量', trigger: 'blur' }
        ]
      },
      //字段配置
      fieldColumns: [
        {

          align: 'center',
          dataIndex: 'FiledTitle',
          scopedSlots: { customRender: 'FiledTitle', title: 'FiledTitleName' }

        },
        {

          align: 'center',
          dataIndex: 'FiledType',
          scopedSlots: { customRender: 'FiledType', title: 'FiledTypeName' }

        },
        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ],
      goodsData: [],
      configList: {},
      seeOpen: false,
      iframeSrc: 'http://192.168.1.106:8084/#/pages/scanIndex'

    }
  },
  filters: {},
  created() {
    this.getList()
  },
  computed: {
    rowSelection() {
      return {
        type: 'radio',
        onChange: (selectedRowKeys, selectedRows) => {
          console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows)
          getCatalogueByDataId(selectedRows[0].traceabilityCatalogueId).then(res => {
            this.configList = {
              batchId: this.form.batchId,
              id: selectedRows[0].id,
              sort: selectedRows[0].sort,
              traceabilityManageId: selectedRows[0].id,
              traceabilityManageName: selectedRows[0].traceabilityName,
              catalogueFormList: res.data

            }

          })
        },
        getCheckboxProps: record => ({
          props: {
            disabled: record.name === 'Disabled User', // Column configuration not to be checked
            name: record.name
          }
        })
      }
    }
  },
  watch: {},
  methods: {
    handleOk() {

    },
    /** 查询列表 */
    getList() {
      this.loading = true
      listBatch(this.queryParam).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    /** 查询溯源节点列表 */
    getTraceList() {
      this.traceLoading = true
      listTraceability(this.traceQueryParam).then(response => {
          this.traceList = response.data.records
          this.traceTotal = response.data.total
          this.traceLoading = false
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
    traceOnShowSizeChange(current, pageSize) {
      this.traceQueryParam.size = pageSize
      this.getTraceList()
    },
    traceChangeSize(current, pageSize) {
      this.traceQueryParam.current = current
      this.traceQueryParam.size = pageSize
      this.getTraceList()
    },


    handleAdd() {
      this.title = '新增批次'
      this.open = true
      this.getGoodsList()
    },
    handleUpdate(row) {
      this.title = '生码'
      this.getGoodsList()
      getById(row.batchId).then(response => {
        this.form = response.data
        this.open = true
      }).finally(() => {
      })

    },
    changeStatus(row) {
      updateStatus(row.batchId).then(response => {
        if (response.code == '0') {
          this.$message.success(
            '操作成功',
            3
          )
          this.getList()
        }
      }).finally(() => {
      })

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
    setting(row) {
      this.title = '配置'
      getById(row.batchId).then(response => {
        this.form = response.data
        this.form.configList.forEach(el => {
          el.catalogueFormList.forEach(row => {
            row.FiledTitle = row.title
            row.FiledType = row.type
          })

        })
        this.open = true
      }).finally(() => {
      })

    },
    chooseTrace() {
      this.traceOpen = true
      this.getTraceList()
    },
    onSelectChange(selectedRowKeys) {
      console.log('selectedRowKeys changed: ', selectedRowKeys)
    },
    traceSubmitForm() {
      if (!this.form.configList) {
        this.form.configList = []
        this.form.configList.push(this.configList)
      } else {
        let configList = this.form.configList
        for (var i = 0; i < configList.length; i++) {
          if (configList[i].traceabilityManageId == this.configList.traceabilityManageId) {
            this.form.configList[i] = this.configList
            return false
          }
        }
        this.form.configList.push(this.configList)
      }

      this.form.configList.forEach(el => {
        el.catalogueFormList.forEach(row => {
          row.FiledTitle = row.title
          row.FiledType = row.type
        })

      })
      this.traceOpen = false
    },
    // 取消按钮
    traceCancel() {
      this.traceOpen = false
      this.tracesubmitLoading = false
    },
    /** 创建和修改 */
    submitForm: function() {

      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          let data = JSON.parse(JSON.stringify(this.form))
          if (this.title == '新增批次') {
            addBatch(data).then(response => {
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
          if (this.title == '配置') {
            configure(data).then(response => {
              this.$message.success(
                '配置成功',
                3
              )
              this.cancel()
              this.getList()
            }).finally(() => {
              this.submitLoading = false
            })
          }
          if (this.title == '生码') {
            data.charType = 1
            genCode(data).then(response => {
              this.$message.success(
                '生码成功',
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
    },

    //删除字段
    FiledDelete(index1, index) {
      this.form.configList[index1].catalogueFormList.splice(index, 1)
    },
    downLoad(row) {
      downloadCode(row.batchCode).then(res => {
        if (!res) {
          return
        }
        let url = window.URL.createObjectURL(new Blob([res], { type: 'application/vnd.ms-excel' }))
        let link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', row.batchCode)
        document.body.appendChild(link)
        link.click()
      })
    },
    see(row) {

      this.iframeSrc = 'http://47.93.123.204/trace/#/pages/scanIndex?code=' + row.qrCode
      this.seeOpen = true

    },
    ylCancel() {
      this.seeOpen = false
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

.yl {
  margin-top: -100px !important;
}

.yl :deep(.ant-modal) {
  top: 10px !important;
}

.yl :deep(.ant-modal-body) {
  height: 80vh !important;
}

.hjtitle {
  font-weight: bold;
  margin-top: 10px;
  margin-bottom: 10px;

}

:deep(.ant-form-item) {
  display: flex !important;

}

.statusDiv {
  display: flex;
  justify-content: center;
  align-items: center
}

.green {
  background: green;
  color: green;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  margin-right: 5px;
}

.red {
  background: red;
  color: red;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  margin-right: 5px;
}
</style>
