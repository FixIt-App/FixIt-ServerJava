import { BaseEntity } from './../../shared';

export class Address implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public address?: string,
        public city?: string,
        public country?: string,
        public latitude?: string,
        public longitude?: string,
        public customer?: BaseEntity,
        public works?: BaseEntity[],
    ) {
    }
}
