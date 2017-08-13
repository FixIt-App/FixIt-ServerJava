import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FixItSharedModule } from '../../shared';
import {
    ConfirmationService,
    ConfirmationPopupService,
    ConfirmationComponent,
    ConfirmationDetailComponent,
    ConfirmationDialogComponent,
    ConfirmationPopupComponent,
    ConfirmationDeletePopupComponent,
    ConfirmationDeleteDialogComponent,
    confirmationRoute,
    confirmationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...confirmationRoute,
    ...confirmationPopupRoute,
];

@NgModule({
    imports: [
        FixItSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ConfirmationComponent,
        ConfirmationDetailComponent,
        ConfirmationDialogComponent,
        ConfirmationDeleteDialogComponent,
        ConfirmationPopupComponent,
        ConfirmationDeletePopupComponent,
    ],
    entryComponents: [
        ConfirmationComponent,
        ConfirmationDialogComponent,
        ConfirmationPopupComponent,
        ConfirmationDeleteDialogComponent,
        ConfirmationDeletePopupComponent,
    ],
    providers: [
        ConfirmationService,
        ConfirmationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FixItConfirmationModule {}
