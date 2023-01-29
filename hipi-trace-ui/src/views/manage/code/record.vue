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
              <!-- <a-button type="primary" @click="handleAdd()"  v-hasPermi="['system:user:add']">
                <a-icon type="plus" />新增
              </a-button> -->
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


                <div v-if="record.status == '1'" class='statusDiv'><span class='green'></span>启用</div>
                <div v-if="record.status == '2'" class='statusDiv'><span class='red'></span>禁用</div>

              </template>

              <template slot='qrCodeImg' slot-scope='text,record,index'>
                <div class='imgBox'>
                  <div class='Qrcode' :ref="'qrcode'+index"></div>
                </div>
              </template>

              <template slot='operation' slot-scope='text, record'>
                <a @click='changeStatus(record)'>
                  <a-icon type='form' />
                  <span v-if="record.status == '1'">禁用</span>
                  <span v-if="record.status == '2'">启用</span>
                </a>
                <a-divider type='vertical' />
                <a @click='handleUpdate(record)'>
                  <a-icon type='eye' />
                  详情
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


    </page-header-wrapper>
  </div>
</template>

<script>

import { listCodeRecord } from '@/api/code/index'
import { selectList } from '@/api/category/index'
import { tableMixin } from '@/store/table-mixin'
import { PlusOutlined } from '@ant-design/icons-vue'
import Qrcode from 'qrcodejs2'

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
      loading: false,
      total: 0,
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
          title: '溯源码',
          dataIndex: 'qrCode',
          align: 'center',
          width: '200px'
        },
        {
          title: '二维码',
          dataIndex: 'qrCodeImg',
          scopedSlots: { customRender: 'qrCodeImg' },
          align: 'center'
        }

      ],
      visible: true


    }
  },
  filters: {},
  created() {
    this.getList()
  },
  computed: {},
  watch: {},
  methods: {
    handleOk() {

    },
    /** 查询列表 */
    getGoodsList() {
      selectList().then(response => {
          this.goodsData = response.data
        }
      )
    },
    /** 查询列表 */
    getList() {
      this.loading = true
      listCodeRecord(this.queryParam).then(response => {
          this.list = response.data.records
          this.$nextTick(() => {
            this.list.forEach((el, index) => {
              let a = 'qrcode' + index
              console.log(this.$refs[a])

              new Qrcode(this.$refs[a], {
                // text 需要转二维码的内容 可以是文本也可以是一个链接 是链接会直接跳走
                text: el.qrCode,
                width: 80,
                height: 80,
                colorDark: '#333',//二维码颜色
                colorLight: '#fff',//二维码背景颜色
                correctLevel: Qrcode.CorrectLevel.L//容错率,L/M/H
              })
            })
          })

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

.imgBox {
  display: flex;
  justify-content: center;
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
