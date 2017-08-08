import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { WorkType } from './work-type.model';
import { WorkTypePopupService } from './work-type-popup.service';
import { WorkTypeService } from './work-type.service';

@Component({
    selector: 'jhi-work-type-delete-dialog',
    templateUrl: './work-type-delete-dialog.component.html'
})
export class WorkTypeDeleteDialogComponent {

    workType: WorkType;

    constructor(
        private workTypeService: WorkTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.workTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'workTypeListModification',
                content: 'Deleted an workType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-work-type-delete-popup',
    template: ''
})
export class WorkTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private workTypePopupService: WorkTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.workTypePopupService
                .open(WorkTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
