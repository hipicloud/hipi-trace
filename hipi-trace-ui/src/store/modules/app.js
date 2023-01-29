import storage from 'store'
import { formConf } from '@/components/generator/config'
import {
  APP_LANGUAGE,
  DRAWING_ID,
  DRAWING_ITEMS,
  DRAWING_ITEMS_VERSION,
  DRAWING_ITEMS_VERSION_KEY,
  FORM_CONF,
  HIDE_FOOTER,
  LANG,
  SIDE_COLLAPSED,
  TABLE_BORDERED,
  TABLE_SIZE,
  TOGGLE_COLOR,
  TOGGLE_CONTENT_WIDTH,
  TOGGLE_FIXED_HEADER,
  TOGGLE_FIXED_SIDEBAR,
  TOGGLE_LAYOUT,
  TOGGLE_MOBILE_TYPE,
  TOGGLE_MULTI_TAB,
  TOGGLE_NAV_THEME,
  TOGGLE_WEAK,
  TREE_NODE_ID
} from '@/store/mutation-types'
import { loadLanguageAsync } from '@/locales'

const app = {
  state: {
    sideCollapsed: false,
    isMobile: false,
    theme: 'dark',
    layout: '',
    contentWidth: '',
    fixedHeader: false,
    fixedSidebar: false,
    color: '',
    weak: false,
    multiTab: true,
    lang: 'zh-CN',
    _antLocale: {},
    tableSize: 'default',
    tableBordered: false,
    hideFooter: false,
    drawingItems: null,
    drawingItemsVersion: DRAWING_ITEMS_VERSION,
    idGlobal: 100,
    treeNodeId: 100,
    formConf,
    lang: 'en-US',
    _antLocale: {}
  },
  mutations: {
    [SIDE_COLLAPSED]: (state, type) => {
      state.sideCollapsed = type
      storage.set(SIDE_COLLAPSED, type)
    },
    [TOGGLE_MOBILE_TYPE]: (state, isMobile) => {
      state.isMobile = isMobile
    },
    [TOGGLE_NAV_THEME]: (state, theme) => {
      state.theme = theme
      storage.set(TOGGLE_NAV_THEME, theme)
    },
    [TOGGLE_LAYOUT]: (state, mode) => {
      state.layout = mode
      storage.set(TOGGLE_LAYOUT, mode)
    },
    [TOGGLE_FIXED_HEADER]: (state, mode) => {
      state.fixedHeader = mode
      storage.set(TOGGLE_FIXED_HEADER, mode)
    },
    [TOGGLE_FIXED_SIDEBAR]: (state, mode) => {
      state.fixedSidebar = mode
      storage.set(TOGGLE_FIXED_SIDEBAR, mode)
    },
    [TOGGLE_CONTENT_WIDTH]: (state, type) => {
      state.contentWidth = type
      storage.set(TOGGLE_CONTENT_WIDTH, type)
    },
    [TOGGLE_COLOR]: (state, color) => {
      state.color = color
      storage.set(TOGGLE_COLOR, color)
    },
    [TOGGLE_WEAK]: (state, mode) => {
      state.weak = mode
      storage.set(TOGGLE_WEAK, mode)
    },
    [APP_LANGUAGE]: (state, lang, antd = {}) => {
      state.lang = lang
      state._antLocale = antd
      storage.set(APP_LANGUAGE, lang)
    },
    [TOGGLE_MULTI_TAB]: (state, bool) => {
      storage.set(TOGGLE_MULTI_TAB, bool)
      state.multiTab = bool
    },
    [TABLE_SIZE]: (state, tableSize) => {
      state.tableSize = tableSize
      storage.set(TABLE_SIZE, tableSize)
    },
    [TABLE_BORDERED]: (state, tableBordered) => {
      state.tableBordered = tableBordered
      storage.set(TABLE_BORDERED, tableBordered)
    },
    [HIDE_FOOTER]: (state, hideFooter) => {
      state.hideFooter = hideFooter
      storage.set(HIDE_FOOTER, hideFooter)
    },
    [DRAWING_ITEMS]: (state, drawingItems) => {
      state.drawingItems = drawingItems
      storage.set(DRAWING_ITEMS, drawingItems)
    },
    [DRAWING_ITEMS_VERSION_KEY]: (state, drawingItemsVersion) => {
      state.drawingItemsVersion = drawingItemsVersion
      storage.set(DRAWING_ITEMS_VERSION_KEY, drawingItemsVersion)
    },
    [DRAWING_ID]: (state, idGlobal) => {
      state.idGlobal = idGlobal
      storage.set(DRAWING_ID, idGlobal)
    },
    [TREE_NODE_ID]: (state, treeNodeId) => {
      state.treeNodeId = treeNodeId
      storage.set(TREE_NODE_ID, treeNodeId)
    },
    [FORM_CONF]: (state, formConfValue) => {
      state.formConf = formConfValue
      storage.set(FORM_CONF, formConfValue)
    },
    [LANG]: (state, lang, antd = {}) => {
      state.lang = lang
      state._antLocale = antd
      storage.set(LANG, lang)
    }
  },
  actions: {
    setLang({ commit }, lang) {
      return new Promise((resolve, reject) => {
        commit(APP_LANGUAGE, lang)
        loadLanguageAsync(lang).then(() => {
          resolve()
        }).catch((e) => {
          reject(e)
        })
      })
    }
  }
}

export default app
