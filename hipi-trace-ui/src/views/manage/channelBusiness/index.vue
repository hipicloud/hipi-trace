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
                <a-form-item label='渠道商编码'>
                  <a-input v-model='queryParam.code' placeholder='请输入' allow-clear />
                </a-form-item>
              </a-col>
              <a-col :md='8' :sm='24'>
                <a-form-item label='渠道商名称'>
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
        </div>
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


                <div v-if="record.status == '0'" class='green'>启用</div>
                <div v-else class='red'>关闭</div>

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
        :width='900'
      >

        <a-form-model ref='form' :model='form' :rules='rules'>
          <a-form-model-item label='渠道商目录' prop='distributorCatalogueId'>
            <a-tree-select
              v-model='form.distributorCatalogueId'
              style='width: 200px'
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data='treeOptions'
              placeholder='请选择'
              :replaceFields='replaceFields'
              tree-default-expand-all
              @change='changeCatalogue'
            >
            </a-tree-select>
          </a-form-model-item>
          <a-form-model-item label='渠道商编码' prop='distributorCode'>
            <a-input v-model='form.distributorCode' :clearable='true' />
          </a-form-model-item>
          <a-form-model-item label='渠道商名称' prop='distributorName'>
            <a-input v-model='form.distributorName' :clearable='true' />
          </a-form-model-item>
        </a-form-model>
        <a-form-model ref='customForm' :model='customForm'>
          <a-form-model-item v-for='(item,index) in customFormList' :key='index' :label='item.title' :prop='item.id'>
            <a-input v-model='customForm[item.id]' :clearable='true' v-if='item.type==1' />
            <a-input v-model='customForm[item.id]' :clearable='true' type='textarea' v-if='item.type==2' />
            <a-upload
              :action='uploadImgUrl'
              :list-type="item.type==3?'picture-card':item.type==4?'picture':''"
              :file-list='item.fileList'
              :headers='headers'
              :beforeUpload='((val,UpFileList)=>{beforeUpload(val,UpFileList,index,item)})'
              @preview='handlePreview'
              @change='((val)=>{handleChange(val,index,item.id)})'

              v-if='item.type==3||item.type==4'
            >
              <div v-if='item.fileList&&item.fileList.length < 8&&item.type==3'>
                <a-icon type='plus' />
                <div class='ant-upload-text'>
                  图片上传
                </div>
              </div>
              <a-button v-if='item.type==4'>
                <a-icon type='plus' />
                视频上传
              </a-button>
            </a-upload>
            <a-modal :visible='previewVisible' :footer='null' @cancel='handleCancel'>
              <img alt='example' style='width: 100%' :src='previewUrl' v-if='uploadType==3' />
              <video width='100%' controls type='video' id='video' v-if='uploadType==4'>
                <source :src='previewUrl' type='video/mp4' />
                <object :data='previewUrl' width='100%'>
                  <embed :src='previewUrl' width='100%' />
                </object>
                您的浏览器不支持video标签哦，请联系管理员
              </video>
            </a-modal>
          </a-form-model-item>
        </a-form-model>
        <div class='titleDiv'>
          <label class='title'>该渠道商销售区域/商品</label>
          <a-button type='primary' @click='addGoods'>
            <a-icon type='plus' />
            新增
          </a-button>
        </div>
        <a-table
          :columns='goodsColumns'
          :dataSource='form.goodsList'
          :pagination='false'
        >

          <template slot='operation' slot-scope='text, record,index'>
            <a @click='editGoods(index,record)'>
              编辑
            </a>
            <a @click='goodsDelete(index)'>
              删除
            </a>

          </template>

        </a-table>
      </a-modal>

      <a-modal
        title='新增区域商品'
        :visible='goodsOpen'
        @ok='confirmGoods'
        @cancel='goodsCancel'
        :width='800'
      >
        <a-form-model ref='goodsForm' :model='goodsForm' :rules='goodsRules'>
          <a-form-model-item label='销售区域' prop='area'>
            <a-cascader
              :fieldNames="{ label: 'name', value: 'code', children: 'children' }"
              :options='areaData'
              placeholder='请选择销售区域'
              v-model='goodsForm.area'
              @change='onChange'
            />
          </a-form-model-item>
        </a-form-model>

        <a-transfer
          :titles="['可选商品', '已选商品']"
          :dataSource='goodsData'
          :target-keys='targetKeys'
          :show-search='true'
          :filter-option='(inputValue, item) => item.title.indexOf(inputValue) !== -1'
          :show-select-all='false'
          @change='onChange1'
          @search='handleSearch'
        >
          <template
            slot='children'
            slot-scope='{
            props: { direction, filteredItems, selectedKeys, disabled: listDisabled },
            on: { itemSelectAll, itemSelect },
          }'
          >
            <a-table
              :row-selection='
              getRowSelection({
                disabled: listDisabled,
                  selectedKeys,
                  itemSelectAll,
                  itemSelect,
              })
            '
              :columns="direction === 'left' ? leftColumns : rightColumns"
              :data-source='filteredItems'
              size='small'
              :pagination='false'
              :scroll='{y: 205}'
              :style="{ pointerEvents: listDisabled ? 'none' : null }"
            />
          </template>
        </a-transfer>

      </a-modal>


    </page-header-wrapper>
  </div>


</template>

<script>

import {
  addChannelBusiness,
  delChannelBusiness,
  getById,
  listChannelBusiness,
  treeList,
  updateChannelBusiness
} from '@/api/channelBusiness/index'
import { selectList } from '@/api/category/index'
import { getCatalogueByDataId } from '@/api/form/index'
import { tableMixin } from '@/store/table-mixin'
import storage from 'store'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import areaData from '@/utils/city'

const originTargetKeys = []

const TableColumns = [

  {
    dataIndex: 'categoryCode',
    title: '商品编号',
    align: 'center'
  },
  {
    dataIndex: 'categoryName',
    title: '商品名称',
    align: 'center'
  }
]

export default {
  name: 'channelbusiness',
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
          title: '渠道商目录编号',
          dataIndex: 'distributorCatalogueCode',
          align: 'center'
        },
        {
          title: '渠道商目录',
          dataIndex: 'distributorCatalogueName',
          align: 'center'
        },
        {
          title: '渠道商编码',
          dataIndex: 'distributorCode',
          align: 'center'
        },
        {
          title: '渠道商名称',
          dataIndex: 'distributorName',
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
        distributorCode: undefined,
        categorName: undefined,
        distributorCatalogueId: undefined,
        goodsList: []

      },
      customFormList: [],//动态字段
      customForm: {},
      uploadImgUrl: process.env.VUE_APP_BASE_API + '/common/upload',
      headers: { Authorization: 'Bearer ' + storage.get(ACCESS_TOKEN) },
      previewVisible: false,
      previewUrl: '',
      uploadType: '',
      open: false,
      submitLoading: false,
      title: '新增渠道商',
      node: '',
      rules: {
        distributorCatalogueId: [
          { required: true, message: '渠道商目录不能为空', trigger: 'change' }
        ],
        distributorCode: [
          { required: true, message: '渠道商编码不能为空', trigger: 'blur' }
        ],
        distributorName: [
          { required: true, message: '渠道商名称不能为空', trigger: 'blur' }
        ]
      },
      goodsOpen: false,
      goodsForm: {},
      goodsLoading: false,
      editIndex: '',
      checkForm: {},
      goodsRules: {
        area: [
          { required: true, message: '请选择销售区域', trigger: 'change' }
        ]
      },
      areaData: areaData,
      goodsColumns: [
        {
          title: '省级',
          dataIndex: 'provinceName',
          align: 'center'
        },
        {
          title: '市级',
          dataIndex: 'cityName',
          align: 'center'
        },
        {
          title: '县级',
          dataIndex: 'countyName',
          align: 'center'
        },
        {
          title: '商品',
          dataIndex: 'goodsName',
          align: 'center'
        },


        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ],
      goodsData: [],
      targetKeys: originTargetKeys,
      disabled: false,
      leftColumns: TableColumns,
      rightColumns: TableColumns,
      rootSubmenuKeys: [],
      openKeys: []


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
      listChannelBusiness(this.queryParam).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    /** 查询渠道商下拉树结构 */
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
        title: '确认删该渠道商吗?',
        content: '当前渠道商编号为' + row.code + '的数据',
        onOk() {
          console.log(row)
          return delChannelBusiness(row.id)
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
      this.title = '新增渠道商'
      this.open = true

    },
    handleUpdate(row) {
      this.getTreeselect()
      this.title = '修改渠道商'

      getById(row.id).then(response => {
        this.form = response.data
        this.customFormList = response.data.catalogueFormList
        this.customForm = response.data.dataJson
        for (let key in this.customForm) {
          if (Array.isArray(this.customForm[key])) {
            this.customFormList.forEach(el => {
              if (el.id == key) {
                el.fileList = this.customForm[key]
                el.fileList.forEach(row => {
                  row.url = process.env.VUE_APP_BASE_API + row.url
                })
              }
            })
          }

        }


        this.customFormList.forEach(el => {
          if (!el.fileList) {
            el.fileList = []
          }
        })
        this.$set(this.form)
        this.open = true
      }).finally(() => {
      })


    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
      this.customFormList = []

    },
    reset() {
      this.form = {
        distributorCode: undefined,
        categorName: undefined,
        distributorCatalogueId: undefined,
        goodsList: []
      }
      this.customForm = {}
    },
    //改变目录
    changeCatalogue(value) {
      getCatalogueByDataId(value).then(res => {
        this.customFormList = res.data
        this.customFormList.forEach(el => {
          if (!el.fileList) {
            el.fileList = []
          }
        })
      })
    },
    handleCancel() {
      this.previewVisible = false
    },
    beforeUpload(file, UpFileList, index, item) {
      return new Promise((resolve, reject) => {
        // file：上传单个文件时候的文件内容，UpFileList：上传多个文件时的文件内容组成的数组,index是索引，item是当前表单的内容
        // 1、控制文件数量
        if (this.customFormList[index].fileList.length + UpFileList.length > 10) {
          this.$message.warning('超过文件上传数量限制')
          return reject(false)
        }
        // 2、控制上传的文件大小
        else if (file.size > 1073741824) {
          this.$message.warning('文件大小超过最大限度1G')
          return reject(false)
        }
        // 3、控制上传文件不能为空
        else if (file.size === 0) {
          this.$message.warning('所选信息中存在空文件或目录，请重新选择')
          return reject(false)
        }
        //4、控制已上传文件不重复
        else if (this.customFormList[index].fileList.length > 0) {
          this.customFormList[index].fileList.map(item => {
            if (item.name === file.name) {
              this.$message.warning('不允许重复上传')
              return reject(false)
            }
          })
        }
        // 5、控制上传文件的类型 arr是上传类型的白名单
        else if (item.type == 3 || item.type == 4) {

          const type = file.name.slice(file.name.lastIndexOf('.') + 1).toLowerCase()
          if (item.type == 3) {
            var arr = ['.jpg', '.png', '.jpeg']
          }
          if (item.type == 4) {
            var arr = ['.mp4']
          }
          if (!arr.includes('.' + type)) {

            if (item.type == 3) {
              this.$message.warning(`不支持以 .${type} 扩展类型的图片上传!`)
              return reject(false)
            }
            if (item.type == 4) {
              this.$message.warning(`不支持以 .${type} 扩展类型的视频上传!`)
              return reject(false)
            }

          }
        } else {
          return resolve()
        }
      })

    },
    async handlePreview(file) {
      if (file.type == 'video/mp4') {
        this.uploadType = 4
      } else {
        this.uploadType = 3
      }
      if (!file.url && !file.preview) {
        await this.getBase64(file.originFileObj).then(res => {
          file.preview = res
        })
      }
      this.previewUrl = file.url || 'data:image/jpg;base64,' + file.preview
      this.previewVisible = true
    },
    getBase64(file) {
      return new Promise((resolve, reject) => {
        let reader = new FileReader()
        let fileResult = ''
        reader.readAsDataURL(file)
        //开始
        reader.onload = function() {
          fileResult = reader.result.split(',')[1]
        }
        //失败
        reader.onerror = function(error) {
          reject(error)
        }
        //结束
        reader.onloadend = function() {
          resolve(fileResult)
        }
      })
    },
    handleChange(file, index, prop) {
      let item = this.customFormList[index]
      item.fileList = file.fileList
      this.$set(this.customFormList, index, item)
      if (file.file.status == 'done' && file.file.response) {
        this.customForm[prop] = []
        file.fileList.forEach(el => {
          this.customForm[prop].push({
            name: el.name,
            url: el.url ? el.url : el.response.fileName,
            uid: el.uid,
            status: el.status,
            type: el.type
          })
        })

      }


    },
    uploadDelete(file, index, item) {
      var index = this.customFormList[index].indexOf(file)
      var newList = this.customFormList[index].slice()
      newList.splice(index, 1)
      this.customFormList[index] = newList
      this.customForm[item.id] = this.customForm[item.id].map(el => {
        if (el.name != file.file.name) {
          return el
        }
      })


    },
    /**
     * 商品新增
     */
    addGoods() {
      this.goodsOpen = true
      this.targetKeys = []
      this.editIndex = ''
      this.getGoodsList()
    },
    /** 查询列表 */
    getGoodsList() {
      this.goodsLoading = true
      selectList().then(response => {
          this.goodsData = response.data
          this.goodsData.forEach((el, index) => {
            el.key = el.id.toString()
          })
          this.goodsLoading = false
        }
      )
    },
    onChange(value, selectedOptions) {
      this.checkForm.provinceName = selectedOptions[0].name
      this.checkForm.provinceCode = selectedOptions[0].code
      this.checkForm.cityName = selectedOptions[1].name
      this.checkForm.cityCode = selectedOptions[1].code
      this.checkForm.countyName = selectedOptions[2].name
      this.checkForm.countyCode = selectedOptions[2].code

    },
    confirmGoods() {
      this.$refs.goodsForm.validate(valid => {
        if (valid) {
          let checkGoods = []
          let checkGoodsName = []
          let targetKeys = this.targetKeys
          targetKeys.forEach(el => {
            this.goodsData.forEach(row => {
              if (row.id == el) {
                checkGoodsName.push(row.categoryName)
                let item = {
                  goodsId: row.id,
                  goodsName: row.categoryName
                }
                checkGoods.push(item)
              }
            })
          })

          let pushItem = {
            provinceName: this.checkForm.provinceName,
            provinceCode: this.checkForm.provinceCode,
            cityName: this.checkForm.cityName,
            cityCode: this.checkForm.cityCode,
            countyName: this.checkForm.countyName,
            countyCode: this.checkForm.countyCode,
            goodsName: checkGoodsName.join('|'),
            goodsInfoList: checkGoods
          }
          console.log(this.editIndex)
          if (this.editIndex === '') {
            console.log('121331313')
            this.form.goodsList.push(pushItem)

          } else {
            console.log('666666')
            this.form.goodsList[this.editIndex] = pushItem
          }
          this.goodsCancel()

        }
      })
    },
    goodsCancel() {
      this.goodsOpen = false
      this.goodsForm = {}

    },
    goodsDelete(index) {
      this.form.goodsList.splice(index, 1)
    },
    editGoods(index, record) {
      this.goodsOpen = true
      this.getGoodsList()
      this.editIndex = index
      this.goodsForm.area = [record.provinceCode, record.cityCode, record.countyCode]
      this.targetKeys = record.goodsInfoList.map(el => {
        return el.goodsId
      })
    },
    /** 创建和修改 */
    submitForm: function() {

      this.$refs.form.validate(valid => {
        if (valid) {

          this.submitLoading = true
          let data = JSON.parse(JSON.stringify(this.form))
          data.dataJson = this.customForm
          this.treeOptions.forEach((el) => {
            if (el.id = data.distributorCatalogueId) {
              data.distributorCatalogueCode = el.code
            }
          })

          if (this.title == '新增渠道商') {
            addChannelBusiness(data).then(response => {
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
            delete data.catalogueFormList
            updateChannelBusiness(data).then(response => {
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
    },
    onChange1(nextTargetKeys, direction, moveKeys) {

      this.targetKeys = nextTargetKeys
      console.log(this.targetKeys)

    },
    // 搜索框
    handleSearch(dir, value) {

    },
    getRowSelection({ disabled, selectedKeys, itemSelectAll, itemSelect }) {
      console.log(selectedKeys)
      return {
        getCheckboxProps: (item) => ({ props: { disabled: disabled || item.disabled } }),
        onSelectAll(selected, selectedRows) {
          if (selected == true) {
            const treeSelectedKeys = selectedRows.filter((item) => !item.disabled).map(({ key }) => key)

            itemSelectAll(treeSelectedKeys, selected)
          } else if (selected == false) {
            let treeSelectedKeys = this.selectedRowKeys

            itemSelectAll(treeSelectedKeys, selected)
          }

          // console.log(decodeDeltasqqq);
        },
        onSelect({ key }, selected) {
          console.log(key, selected)
          itemSelect(key, selected)
        },

        selectedRowKeys: selectedKeys
      }
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

.titleDiv {
  display: flex;
  justify-content: space-between;
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
  left: 15px;

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
  left: 15px;

}
</style>
