import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FixItSharedModule } from '../../shared';
import {
    WorkTypeCategoryService,
    WorkTypeCategoryPopupService,
    WorkTypeCategoryComponent,
    WorkTypeCategoryDetailComponent,
    WorkTypeCategoryDialogComponent,
    WorkTypeCategoryPopupComponent,
    WorkTypeCategoryDeletePopupComponent,
    WorkTypeCategoryDeleteDialogComponent,
    workTypeCategoryRoute,
    workTypeCategoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...workTypeCategoryRoute,
    ...workTypeCategoryPopupRoute,
];

@NgModule({
    imports: [
        FixItSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WorkTypeCategoryComponent,
        WorkTypeCategoryDetailComponent,
        WorkTypeCategoryDialogComponent,
        WorkTypeCategoryDeleteDialogComponent,
        WorkTypeCategoryPopupComponent,
        WorkTypeCategoryDeletePopupComponent,
    ],
    entryComponents: [
        WorkTypeCategoryComponent,
        WorkTypeCategoryDialogComponent,
        WorkTypeCategoryPopupComponent,
        WorkTypeCategoryDeleteDialogComponent,
        WorkTypeCategoryDeletePopupComponent,
    ],
    providers: [
        WorkTypeCategoryService,
        WorkTypeCategoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FixItWorkTypeCategoryModule {}
