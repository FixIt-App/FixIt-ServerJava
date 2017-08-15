import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ConfirmationComponent } from './confirmation.component';
import { ConfirmationDetailComponent } from './confirmation-detail.component';
import { ConfirmationPopupComponent } from './confirmation-dialog.component';
import { ConfirmationDeletePopupComponent } from './confirmation-delete-dialog.component';

export const confirmationRoute: Routes = [
    {
        path: 'confirmation',
        component: ConfirmationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.confirmation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'confirmation/:id',
        component: ConfirmationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.confirmation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const confirmationPopupRoute: Routes = [
    {
        path: 'confirmation-new',
        component: ConfirmationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.confirmation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'confirmation/:id/edit',
        component: ConfirmationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.confirmation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'confirmation/:id/delete',
        component: ConfirmationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fixItApp.confirmation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
