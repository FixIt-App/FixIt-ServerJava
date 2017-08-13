import { BaseEntity, User } from './../../shared';

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public phone?: string,
        public user?: User,
        public addresses?: BaseEntity[],
        public works?: BaseEntity[],
        public confirmations?: BaseEntity[],
    ) {
    }
}
