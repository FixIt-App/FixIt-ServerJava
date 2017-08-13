import { BaseEntity } from './../../shared';

export class Work implements BaseEntity {
    constructor(
        public id?: number,
        public time?: any,
        public description?: string,
        public asap?: boolean,
        public worker?: BaseEntity,
        public customer?: BaseEntity,
        public address?: BaseEntity,
        public worktype?: BaseEntity,
    ) {
        this.asap = false;
    }
}
