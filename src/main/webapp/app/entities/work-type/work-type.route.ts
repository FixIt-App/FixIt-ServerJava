import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { WorkTypeComponent } from './work-type.component';
import { WorkTypeDetailComponent } from './work-type-detail.component';
import { WorkTypePopupComponent } from './work-type-dialog.component';
import { WorkTypeDeletePopupComponent } from './work-type-delete-dialog.component';

export const workTypeRoute: Routes = [
    {
        path: 'work-type',
        component: WorkTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'work-type/:id',
        component: WorkTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const workTypePopupRoute: Routes = [
    {
        path: 'work-type-new',
        component: WorkTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'work-type/:id/edit',
        component: WorkTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'work-type/:id/delete',
        component: WorkTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.workType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
