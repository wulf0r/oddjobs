import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../pages/HomePage.vue'
import OddJobAdmin from '../pages/OddJobAdmin.vue'
const router = createRouter({
    history: createWebHistory(),

    routes: [
        {
            path: '/',
            component: HomePage
        },
        {
            path: '/admin',
            component: OddJobAdmin
        }
    ]
})

export default router