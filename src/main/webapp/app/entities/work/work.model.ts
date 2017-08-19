import { BaseEntity } from './../../shared';

const enum WorkState {
    'ORDERED',
    'SCHEDULED',
    'FINISHED',
    'FAILED',
    'CANCELED',
    'IN_PROGRESS'
}

export class Work implements BaseEntity {
    constructor(
        public id?: number,
        public time?: any,
        public description?: string,
        public asap?: boolean,
        public state?: WorkState,
        public worker?: BaseEntity,
        public customer?: BaseEntity,
        public address?: BaseEntity,
        public worktype?: BaseEntity,
    ) {
        this.asap = false;
    }
}
