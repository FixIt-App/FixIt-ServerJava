import { BaseEntity, User } from './../../shared';

export class Worker implements BaseEntity {
    constructor(
        public id?: number,
        public documentId?: string,
        public phone?: string,
        public rh?: string,
        public user?: User,
        public works?: BaseEntity[],
        public workTypes?: BaseEntity[],
    ) {
    }
}
