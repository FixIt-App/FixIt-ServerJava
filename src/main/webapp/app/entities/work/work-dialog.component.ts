import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Work } from './work.model';
import { WorkPopupService } from './work-popup.service';
import { WorkService } from './work.service';

@Component({
    selector: 'jhi-work-dialog',
    templateUrl: './work-dialog.component.html'
})
export class WorkDialogComponent implements OnInit {

    work: Work;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private workService: WorkService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.work.id !== undefined) {
            this.subscribeToSaveResponse(
                this.workService.update(this.work));
        } else {
            this.subscribeToSaveResponse(
                this.workService.create(this.work));
        }
    }

    private subscribeToSaveResponse(result: Observable<Work>) {
        result.subscribe((res: Work) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Work) {
        this.eventManager.broadcast({ name: 'workListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-work-popup',
    template: ''
})
export class WorkPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private workPopupService: WorkPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.workPopupService
                    .open(WorkDialogComponent as Component, params['id']);
            } else {
                this.workPopupService
                    .open(WorkDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
