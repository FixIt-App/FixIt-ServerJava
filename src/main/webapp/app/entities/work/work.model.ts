import { BaseEntity } from './../../shared';

export class Work implements BaseEntity {
    constructor(
        public id?: number,
        public time?: any,
        public description?: string,
        public asap?: boolean,
        public workers?: BaseEntity[],
        public customers?: BaseEntity[],
        public addresses?: BaseEntity[],
        public worktypes?: BaseEntity[],
    ) {
        this.asap = false;
    }
}
