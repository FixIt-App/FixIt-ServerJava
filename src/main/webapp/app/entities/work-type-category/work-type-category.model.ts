import { BaseEntity } from './../../shared';

export class WorkTypeCategory implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public order?: number,
        public workTypes?: BaseEntity[],
    ) {
    }
}
