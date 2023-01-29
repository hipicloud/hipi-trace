<template>
  <div class='page'>
    <page-header-wrapper>
      <a-card :bordered='false'>
      </a-card>
      <a-card :bordered='false'>

        <!-- 条件搜索 -->
        <div class='table-page-search-wrapper'>
          <a-form layout='inline'>
            <a-row :gutter='48'>
              <a-col :md='8' :sm='24'>
                <a-form-item label='品类编码'>
                  <a-input v-model='queryParam.code' placeholder='请输入' allow-clear />
                </a-form-item>
              </a-col>
              <a-col :md='8' :sm='24'>
                <a-form-item label='品类名称'>
                  <a-input v-model='queryParam.name' placeholder='请输入' allow-clear />
                </a-form-item>
              </a-col>
              <a-col :md='!advanced && 8 || 24' :sm='24'>
                  <span class='table-page-search-submitButtons'
                        :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                    <a-button type='primary' @click='handleQuery'><a-icon type='search' />查询</a-button>
                    <a-button style='margin-left: 8px' @click='resetQuery'><a-icon type='redo' />重置</a-button>
                  </span>
              </a-col>
            </a-row>
          </a-form>
        </div
          <!-- 操作 -->
        <div class='table-operations'>
          <a-button type='primary' @click='handleAdd()' v-if='queryParam.grade!=5' v-hasPermi="['system:user:add']">
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
            <span slot='status' slot-scope='text, record'>


                <div v-if="record.status == '0'"><span class='green'></span>启用</div>
                <div v-else><span class='red'></span>关闭</div>

            </span>
          <span slot='operation' slot-scope='text, record'>
              <a @click='handleUpdate(record)'>
                <a-icon type='edit' />
                编辑
              </a>
              <a-divider type='vertical' />
              <a @click='handleDelete(record)'>
                <a-icon type='delete' />
                删除
              </a>

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

      </a-card>


      <a-modal
        :title='title'
        :visible='open'
        :confirm-loading='submitLoading'
        @ok='submitForm'
        @cancel='cancel'
      >

        <a-form-model ref='form' :model='form' :rules='rules'>
          <a-form-model-item label='品类目录' prop='categoryCatalogueId'>
            <a-tree-select
              v-model='form.categoryCatalogueId'
              style='width: 200px'
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data='treeOptions'
              placeholder='请选择'
              :replaceFields='replaceFields'
              tree-default-expand-all
            >
            </a-tree-select>
          </a-form-model-item>
          <a-form-model-item label='品类编码' prop='categoryCode'>
            <a-input v-model='form.categoryCode' :clearable='true' />
          </a-form-model-item>
          <a-form-model-item label='品类名称' prop='categoryName'>
            <a-input v-model='form.categoryName' :clearable='true' />
          </a-form-model-item>


        </a-form-model>
      </a-modal>


    </page-header-wrapper>
  </div>


</template>

<script>

import { addCategory, delCategory, getById, listCategory, treeList, updateCategory } from '@/api/category/index'
import { tableMixin } from '@/store/table-mixin'


export default {
  name: 'User',

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
      treeOptions: [{
        id: 0,
        label: '',
        children: []
      }],
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
          title: '品类目录编号',
          dataIndex: 'categoryCatalogueCode',
          align: 'center'
        },
        {
          title: '品类目录',
          dataIndex: 'categoryCatalogueName',
          align: 'center'
        },
        {
          title: '品类编码',
          dataIndex: 'categoryCode',
          align: 'center'
        },
        {
          title: '品类名称',
          dataIndex: 'categoryName',
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
        categoryCode: undefined,
        categorName: undefined,
        categoryCatalogueId: undefined

      },
      open: false,
      submitLoading: false,
      title: '新增品类',
      node: '',
      rules: {
        categoryCatalogueId: [
          { required: true, message: '品类目录不能为空', trigger: 'change' }
        ],
        categoryCode: [
          { required: true, message: '品类编码不能为空', trigger: 'blur' }
        ],
        categoryName: [
          { required: true, message: '品类名称不能为空', trigger: 'blur' }
        ]
      }

    }
  },
  filters: {},
  created() {
    this.getList()
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

    /** 查询列表 */
    getList() {
      this.loading = true
      listCategory(this.queryParam).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    /** 查询品类下拉树结构 */
    getTreeselect() {
      treeList().then(response => {
        this.treeOptions = response.data
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


    /** 删除按钮操作 */
    handleDelete(row) {

      var that = this
      this.$confirm({
        title: '确认删该品类吗?',
        content: '当前品类编号为' + row.code + '的数据',
        onOk() {
          console.log(row)
          return delCategory(row.id)
            .then(() => {
              that.getList()
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
      this.getTreeselect()
      this.title = '新增品类'
      this.open = true

    },
    handleUpdate(row) {
      this.getTreeselect()
      this.title = '修改品类'
      getById(row.id).then(response => {
        this.form = response.data

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
        categoryCode: undefined,
        categorName: undefined,
        categoryCatalogueId: undefined
      }
    },
    /** 创建和修改 */
    submitForm: function() {

      this.$refs.form.validate(valid => {
        if (valid) {

          this.submitLoading = true
          let data = JSON.parse(JSON.stringify(this.form))
          this.treeOptions.forEach((el) => {
            if (el.id = data.categoryCatalogueId) {
              data.categoryCatalogueCode = el.code
            }
          })

          if (this.title == '新增品类') {
            addCategory(data).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.cancel()
              this.getList()
            }).finally(() => {
              this.submitLoading = false
            })
          } else {
            updateCategory(data).then(response => {
              this.$message.success(
                '修改成功',
                3
              )
              this.cancel()
              this.getList()
              this.submitLoading = false
              this.open = false
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

.desginForm1 {
  width: calc(100% - 600px);
  height: 100vh;
  overflow-y: auto;
  position: fixed;
  top: 0px;
  left: 251px;
  z-index: 1002;
  background: #fff;
  display: flex;


}

:deep(.ant-form-item) {
  display: flex !important;

}

.green, .red {
  position: relative;
}

.green {
  background: green;
  color: green;
  width: 5px;
  height: 5px;
  border-radius: 50%;
}

.red {
  background: red;
  color: red;
  width: 5px;
  height: 5px;
  border-radius: 50%;
}
</style>
