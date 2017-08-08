import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FixItSharedModule } from '../../shared';
import {
    WorkService,
    WorkPopupService,
    WorkComponent,
    WorkDetailComponent,
    WorkDialogComponent,
    WorkPopupComponent,
    WorkDeletePopupComponent,
    WorkDeleteDialogComponent,
    workRoute,
    workPopupRoute,
} from './';

const ENTITY_STATES = [
    ...workRoute,
    ...workPopupRoute,
];

@NgModule({
    imports: [
        FixItSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WorkComponent,
        WorkDetailComponent,
        WorkDialogComponent,
        WorkDeleteDialogComponent,
        WorkPopupComponent,
        WorkDeletePopupComponent,
    ],
    entryComponents: [
        WorkComponent,
        WorkDialogComponent,
        WorkPopupComponent,
        WorkDeleteDialogComponent,
        WorkDeletePopupComponent,
    ],
    providers: [
        WorkService,
        WorkPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FixItWorkModule {}
