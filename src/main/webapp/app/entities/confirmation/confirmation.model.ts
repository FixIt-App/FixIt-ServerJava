import { BaseEntity } from './../../shared';

const enum CONFIRMATION_TYPE {
    'EMAIL',
    'SMS'
}

export class Confirmation implements BaseEntity {
    constructor(
        public id?: number,
        public type?: CONFIRMATION_TYPE,
        public expires?: any,
        public code?: string,
        public state?: boolean,
        public customer?: BaseEntity,
    ) {
        this.state = false;
    }
}
