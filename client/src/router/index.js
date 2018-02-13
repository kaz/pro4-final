import Vue from 'vue'
import Router from 'vue-router'

import Index from '@/components/Index'
import Search from '@/components/Search'
import Reservation from '@/components/Reservation'
import Complete from '@/components/Complete'
import Check from '@/components/Check'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/search',
      name: 'Search',
      component: Search
    },
    {
      path: '/reservation',
      name: 'Reservation',
      component: Reservation
    },
    {
      path: '/complete',
      name: 'Complete',
      component: Complete
    },
    {
      path: '/check',
      name: 'Check',
      component: Check
    },
  ]
})
