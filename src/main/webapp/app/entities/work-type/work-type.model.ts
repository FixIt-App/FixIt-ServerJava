import { BaseEntity } from './../../shared';

const enum PriceType {
    'STANDARIZED',
    'NOT_STANDARIZED',
    'UNKNOWN'
}

export class WorkType implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public icon?: string,
        public priceType?: PriceType,
        public price?: number,
        public order?: number,
        public urlName?: string,
        public categories?: BaseEntity[],
        public workers?: BaseEntity[],
        public works?: BaseEntity[],
    ) {
    }
}
