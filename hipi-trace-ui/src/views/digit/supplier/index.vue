<template>
  <div class='page'>
    <page-header-wrapper>
      <a-card :bordered='false'>
      </a-card>
      <a-card :bordered='false'>
        <a-row :gutter='24'>
          <a-col :span='5'>
            <a-button type='primary' @click='handleAddTree()' v-hasPermi="['system:user:add']">
              <a-icon type='plus' />
              新增
            </a-button>
            <!-- 树 -->
            <a-tree
              :tree-data='deptOptions'
              :selectedKeys='selectedKeys'
              :default-selected-keys='defaultSelectedKeys'
              :replaceFields='replaceFields'
              @select='handleNodeClick'
              default-expand-all
              show-icon
            >

              <template slot='plus' slot-scope='record'>
                <a-icon
                  type='plus'
                  class='plusType'
                  style='padding-left: 10px'
                  @click.stop='handleAdd(record, true)'
                ></a-icon>
                <a-icon
                  type='edit'
                  class='editType'
                  style='padding-left: 10px'
                  @click.stop='handleUpdate(record, false)'
                ></a-icon>
                <a-icon
                  type='delete'
                  class='deleteType'
                  style='padding-left: 10px'
                  @click.stop='handleDelete(record)'
                ></a-icon>
              </template>

            </a-tree>
          </a-col>
          <a-col :span='19'>
            <!-- 条件搜索 -->
            <div class='table-page-search-wrapper'>
              <a-form layout='inline'>
                <a-row :gutter='48'>
                  <a-col :md='8' :sm='24'>
                    <a-form-item label='结构编码'>
                      <a-input v-model='queryParam.code' placeholder='请输入' allow-clear />
                    </a-form-item>
                  </a-col>
                  <a-col :md='8' :sm='24'>
                    <a-form-item label='结构名称'>
                      <a-input v-model='queryParam.name' placeholder='请输入' allow-clear />
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
            </div
              <!-- 操作 -->
            <div class='table-operations'>

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


                <div v-if="record.status == '0'" class='statusDiv'><span class='green'></span>启用</div>
                <div v-else class='statusDiv'><span class='red'></span>关闭</div>

              </template>
              <template slot='operation' slot-scope='text, record'>
                <a @click='handleUpdate(record)'>
                  <a-icon type='edit' />
                  编辑
                </a>
                <a-divider type='vertical' />
                <a @click='handleDelete(record)'>
                  <a-icon type='delete' />
                  删除
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

        <a-form-model ref='form' :model='form' :rules='rules'>
          <a-form-model-item label='供应商编码' prop='code'>
            <a-input v-model='form.code' :clearable='true' />
          </a-form-model-item>
          <a-form-model-item label='供应商名称' prop='name'>
            <a-input v-model='form.name' :clearable='true' />
          </a-form-model-item>
          <a-form-model-item label='是否启用'>
            <a-switch v-model='form.status' />
          </a-form-model-item>
          <div>
            <label class='title'>字段配置</label>
            <a-table
              :columns='fieldColumns'
              :dataSource='form.catalogueFormList'
              :pagination='false'
            >
              <template slot='sortName'>
                <i class='toRequired'>*</i>序号
              </template>
              <template slot='FiledTitleName'>
                <i class='toRequired'>*</i>标题
              </template>
              <template slot='FiledTypeName'>
                <i class='toRequired'>*</i>类型
              </template>
              <template slot='sort' slot-scope='text, record,index'>
                <a-form-model-item
                  :prop="'catalogueFormList.' + index + '.sort'"
                  :rules='rules.sort'
                  :label-col='{
                        xs: { span: 0 },
                        sm: { span: 0 },
                        }'
                  :wrapper-col='{
                        xs: { span: 24 },
                        sm: { span: 24 },
                        }'
                >
                  <a-input v-model:value='record.sort' type='number' placeholder='请填写' />
                </a-form-model-item>
              </template>
              <template slot='FiledTitle' slot-scope='text, record,index'>
                <a-form-model-item
                  :prop="'catalogueFormList.' + index + '.FiledTitle'"
                  :rules='rules.FiledTitle'
                  :label-col='{
                        xs: { span: 0 },
                        sm: { span: 0 },
                        }'
                  :wrapper-col='{
                        xs: { span: 24 },
                        sm: { span: 24 },
                        }'
                >
                  <a-input v-model:value='record.FiledTitle' placeholder='请填写' />
                </a-form-model-item>
              </template>
              <template slot='FiledType' slot-scope='text, record,index'>
                <a-form-model-item
                  :prop="'catalogueFormList.' + index + '.FiledType'"
                  :rules='rules.FiledType'
                  :label-col='{
                        xs: { span: 0 },
                        sm: { span: 0 },
                        }'
                  :wrapper-col='{
                        xs: { span: 24 },
                        sm: { span: 24 },
                        }'
                >
                  <a-select v-model='record.FiledType' placeholder='请选择' style='width:120px'>
                    <a-select-option value='1'>文本</a-select-option>
                    <a-select-option value='2'>多行文本</a-select-option>
                    <a-select-option value='3'>图片</a-select-option>
                    <a-select-option value='4'>视频</a-select-option>
                  </a-select>
                </a-form-model-item>
              </template>
              <template slot='operation' slot-scope='text, record,index'>

                <a @click='FiledDelete(index)'>
                  删除
                </a>

              </template>


            </a-table>

            <a-button block @click='addField'>
              <!-- <template #icon>
                <PlusOutlined /> -->
              添加一条
              <!-- </template> -->
            </a-button>
          </div>


        </a-form-model>
      </a-modal>


    </page-header-wrapper>


  </div>


</template>

<script>

import { addSupplier, delSupplier, getById, listSupplier, treeList, updateSupplier } from '@/api/digit/supplier'
import { tableMixin } from '@/store/table-mixin'
import { PlusOutlined } from '@ant-design/icons-vue'
// 数组对象中查找符合目标的对象
const parseArray = function(objArray, key, value) {
  for (let i in objArray) {
    let element = objArray[i]
    if (typeof element === 'object') {
      let result = parseArray(element, key, value)
      if (result) return result
    } else {
      if (i === key) {
        if (element === value) return objArray
      }
    }
  }
}

export default {
  name: 'supplierCatalogue',
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
      selectedKeys: null,
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
          title: '供应商编码',
          dataIndex: 'code',
          align: 'center'
        },
        {
          title: '供应商名称',
          dataIndex: 'name',
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
      form: {
        code: undefined,
        name: undefined,
        status: true,
        catalogueFormList: [{}
        ]

      },
      open: false,
      submitLoading: false,
      title: '新增供应商',
      node: '',
      rules: {
        code: [
          { required: true, message: '供应商编码不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '供应商名称不能为空', trigger: 'blur' }
        ],
        FiledType: [
          { required: true, message: '请填写', trigger: 'blur' }
        ],
        FiledTitle: [
          { required: true, message: '请填写', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请填写', trigger: 'blur' }
        ]
      },
      //字段配置
      fieldColumns: [
        {

          align: 'center',
          dataIndex: 'sort',
          scopedSlots: { customRender: 'sort', title: 'sortName' }
        },
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
      filterResource: null,
      isAddTree: false
    }
  },
  filters: {},
  created() {
    this.getList()
    this.getTreeselect()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_user_sex').then(response => {
      this.sexOptions = response.data
    })
  },
  computed: {},
  watch: {},
  methods: {

    handleOk() {

    },
    /** 查询列表 */
    getList() {
      this.loading = true
      listSupplier(this.queryParam).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeList().then(response => {
        this.deptOptions = response.data
        if (!this.queryParam.parentId) {
          this.defaultSelectedKeys.push(this.deptOptions[0].id.toString())
          this.queryParam.parentId = this.deptOptions[0].id
          this.queryParam.grade = this.deptOptions[0].grade + 1
          this.getList()
        }
      })
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

    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    handleNodeClick(keys, event) {
      if (this.filterResource) {
        this.$set(this.filterResource, 'scopedSlots', {})
        this.selectedKeys = null
      }
      if (!event.selected) {
        this.node = ''
      } else {
        this.filterResource = parseArray(this.deptOptions, 'id', keys[0])
        this.$set(this.filterResource, 'scopedSlots', { icon: 'plus' })
        this.node = event.selectedNodes[0].data.props
        this.selectedKeys = keys
      }
      this.queryParam.parentId = this.node.id
      this.queryParam.grade = this.node.grade + 1
      this.handleQuery()
    },


    /** 删除按钮操作 */
    handleDelete(row) {

      var that = this
      this.$confirm({
        title: '确认删该供应商吗?',
        content: '当前供应商编号为' + row.code + '的数据',
        onOk() {
          console.log(row)
          return delSupplier(row.id)
            .then(() => {
              that.getList()
              that.getTreeselect()
              that.$message.success(
                '删除成功',
                3
              )
            })
        },
        onCancel() {
          that.$message.success(
            '删除成功',
            3
          )
        }
      })
    },
    handleAdd() {
      this.title = '新增供应商'
      this.open = true
      this.isAddTree = false
    },
    handleAddTree() {
      this.title = '新增供应商'
      this.open = true
      this.isAddTree = true
    },
    handleUpdate(row) {
      this.title = '修改供应商'
      getById(row.id).then(response => {
        this.form = response.data
        this.form.catalogueFormList = this.form.catalogueFormList == null ? [{}] : this.form.catalogueFormList
        this.form.catalogueFormList.forEach(el => {
          el.FiledTitle = el.title
          el.FiledType = el.type

        })
        let status
        if (response.data.status == 0) {
          status = true
        }
        if (response.data.status == 1) {
          status = false
        }
        this.form.status = status
        console.log(this.form)
        this.$set(this.form)
        this.open = true
      }).finally(() => {
      })


    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()

    },
    reset() {
      this.form = {
        code: undefined,
        name: undefined,
        status: true,
        catalogueFormList: [
          { sort: '', FiledTitle: '', FiledType: '' }
        ]
      }
    },
    /** 创建和修改 */
    submitForm: function() {

      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          let data = JSON.parse(JSON.stringify(this.form))
          data.status = this.form.status == true ? 0 : 1
          data.catalogueFormList.forEach(el => {
            el.title = el.FiledTitle
            el.type = el.FiledType
            delete el.FiledTitle
            delete el.FiledType

          })

          if (this.title == '新增供应商') {
            if (this.node != '' || this.isAddTree == false) {
              data.parentId = this.node.id
              data.grade = this.node.grade + 1
            } else {
              data.parentId = 0
              data.grade = 1
            }
            addSupplier(data).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.cancel()
              this.getList()
              this.getTreeselect()
            }).finally(() => {
              this.submitLoading = false
            })
          } else {
            updateSupplier(data).then(response => {
              this.$message.success(
                '修改成功',
                3
              )
              this.cancel()
              this.getList()
              this.getTreeselect()
            }).finally(() => {
              this.submitLoading = false
            })
          }
        } else {
          return false
        }
      })
    },
    //新增字段
    addField() {
      this.form.catalogueFormList.push({})
    },
    //删除字段
    FiledDelete(index) {
      this.form.catalogueFormList.splice(index, 1)
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

/deep/ .ant-tree li span.ant-tree-iconEle {
  width: 14px;
  height: 14px;
  position: absolute;
  right: 70px;
}

/deep/ .ant-tree-node-content-wrapper {
  max-width: 190px;
  overflow: hidden;
  text-overflow: ellipsis;
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
