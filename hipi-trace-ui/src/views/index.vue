<template>
  <page-header-wrapper>
    <template v-slot:content>
      <div class='page-header-content'>
        <div class='avatar'>
          <a-avatar size='large' :src='avatar' />
        </div>
        <div class='content'>
          <div class='content-title'>
            {{ timeFix }}，{{ nickname }}<span class='welcome-text'></span>
          </div>
          <div> {{ postGroup }} | {{ user.dept.deptName }}</div>
        </div>
      </div>
    </template>
    <template v-slot:extraContent>
    </template>
    <!-- 致谢项目 -->
    <div>
      <a-row :gutter='24'>
        <a-col :xl='16' :lg='24' :md='24' :sm='24' :xs='24'>

          <!-- 项目简介 -->
          <a-card
            style='width:100%'
            title='嗨皮云溯源防伪系统简介'
          >
            <p>开源溯源防伪系统、一物一码系统，适用于各种溯源场景的大、中、小、微规模企业的商用级溯源防伪系统</p>
            <blockquote>
              <p>开源溯源防伪系统、一物一码系统，适用于各种溯源场景的大、中、小、微规模企业的商用级溯源防伪系统。可追踪记录产品的生命周期各个环节，把产品的环节信息保存在系统中，品牌保护、产品营销、防止假冒伪劣产品、防窜货、产品追踪溯源。应用的场景领域：快销品行业、种植行业、养殖行业、畜牧行业、加工行业、仓储行业、零售行业等。</p>

            </blockquote>
          </a-card>
        </a-col>

      </a-row>
    </div>
  </page-header-wrapper>
</template>

<script>
import { timeFix } from '@/utils/util'
import { mapGetters } from 'vuex'
import { PageHeaderWrapper } from '@/components/ProLayout'
import { getUserProfile } from '@/api/system/user'

export default {
  name: 'Index',
  components: {
    PageHeaderWrapper
  },
  data() {
    return {
      // 贡献者
      contributors: [],
      // 赞助
      sponsorList: [],
      noTitleKey: 'happy',
      timeFix: timeFix(),
      // 用户信息
      user: {
        dept: {
          deptName: ''
        }
      },
      roleGroup: {},
      postGroup: {},
      // 致谢项目
      projects: []
    }
  },
  computed: {
    ...mapGetters([
      'avatar',
      'nickname'
    ])
  },
  created() {
    this.getUser()
  },
  mounted() {
  },
  methods: {
    // 获取用户信息
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data
        this.roleGroup = response.roleGroup
        this.postGroup = response.postGroup
      })
    },
    onSponsorTabChange(key, type) {
      this[type] = key
    }
  }
}
</script>

<style lang='less' scoped>
@import "./index.less";

blockquote {
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
}

.project-list {

  .card-title {
    font-size: 0;

    a {
      color: rgba(0, 0, 0, 0.85);
      margin-left: 12px;
      line-height: 24px;
      height: 24px;
      display: inline-block;
      vertical-align: top;
      font-size: 14px;

      &:hover {
        color: #1890ff;
      }
    }
  }

  .card-description {
    color: rgba(0, 0, 0, 0.45);
    height: 66px;
    line-height: 22px;
    overflow: hidden;
  }

  .project-item {
    display: flex;
    margin-top: 8px;
    overflow: hidden;
    font-size: 12px;
    height: 20px;
    line-height: 20px;

    a {
      color: rgba(0, 0, 0, 0.45);
      display: inline-block;
      flex: 1 1 0;

      &:hover {
        color: #1890ff;
      }
    }

    .download {
      color: rgba(0, 0, 0, 0.25);
      flex: 0 0 auto;
      float: right;
    }
  }

  .ant-card-meta-description {
    color: rgba(0, 0, 0, 0.45);
    height: 44px;
    line-height: 22px;
    overflow: hidden;
  }
}

.item-group {
  padding: 20px 0 8px 24px;
  font-size: 0;

  a {
    color: rgba(0, 0, 0, 0.65);
    display: inline-block;
    font-size: 14px;
    margin-bottom: 13px;
    width: 25%;
  }
}

.mobile {

  .project-list {

    .project-card-grid {
      width: 100%;
    }
  }

  .more-info {
    border: 0;
    padding-top: 16px;
    margin: 16px 0 16px;
  }

  .headerContent .title .welcome-text {
    display: none;
  }
}

</style>
