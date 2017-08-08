import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { WorkTypeCategoryComponent } from './work-type-category.component';
import { WorkTypeCategoryDetailComponent } from './work-type-category-detail.component';
import { WorkTypeCategoryPopupComponent } from './work-type-category-dialog.component';
import { WorkTypeCategoryDeletePopupComponent } from './work-type-category-delete-dialog.component';

export const workTypeCategoryRoute: Routes = [
    {
        path: 'work-type-category',
        component: WorkTypeCategoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workTypeCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'work-type-category/:id',
        component: WorkTypeCategoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workTypeCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const workTypeCategoryPopupRoute: Routes = [
    {
        path: 'work-type-category-new',
        component: WorkTypeCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workTypeCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'work-type-category/:id/edit',
        component: WorkTypeCategoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workTypeCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'work-type-category/:id/delete',
        component: WorkTypeCategoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workTypeCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
