import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FixItWorkerModule } from './worker/worker.module';
import { FixItCustomerModule } from './customer/customer.module';
import { FixItAddressModule } from './address/address.module';
import { FixItWorkTypeCategoryModule } from './work-type-category/work-type-category.module';
import { FixItWorkModule } from './work/work.module';
import { FixItWorkTypeModule } from './work-type/work-type.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        FixItWorkerModule,
        FixItCustomerModule,
        FixItAddressModule,
        FixItWorkTypeCategoryModule,
        FixItWorkModule,
        FixItWorkTypeModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FixItEntityModule {}
