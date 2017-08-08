import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WorkType } from './work-type.model';
import { WorkTypePopupService } from './work-type-popup.service';
import { WorkTypeService } from './work-type.service';
import { Work, WorkService } from '../work';
import { WorkTypeCategory, WorkTypeCategoryService } from '../work-type-category';
import { Worker, WorkerService } from '../worker';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-work-type-dialog',
    templateUrl: './work-type-dialog.component.html'
})
export class WorkTypeDialogComponent implements OnInit {

    workType: WorkType;
    isSaving: boolean;

    works: Work[];

    worktypecategories: WorkTypeCategory[];

    workers: Worker[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private workTypeService: WorkTypeService,
        private workService: WorkService,
        private workTypeCategoryService: WorkTypeCategoryService,
        private workerService: WorkerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.workService.query()
            .subscribe((res: ResponseWrapper) => { this.works = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.workTypeCategoryService.query()
            .subscribe((res: ResponseWrapper) => { this.worktypecategories = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.workerService.query()
            .subscribe((res: ResponseWrapper) => { this.workers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.workType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.workTypeService.update(this.workType));
        } else {
            this.subscribeToSaveResponse(
                this.workTypeService.create(this.workType));
        }
    }

    private subscribeToSaveResponse(result: Observable<WorkType>) {
        result.subscribe((res: WorkType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: WorkType) {
        this.eventManager.broadcast({ name: 'workTypeListModification', content: 'OK'});
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

    trackWorkById(index: number, item: Work) {
        return item.id;
    }

    trackWorkTypeCategoryById(index: number, item: WorkTypeCategory) {
        return item.id;
    }

    trackWorkerById(index: number, item: Worker) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-work-type-popup',
    template: ''
})
export class WorkTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private workTypePopupService: WorkTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.workTypePopupService
                    .open(WorkTypeDialogComponent as Component, params['id']);
            } else {
                this.workTypePopupService
                    .open(WorkTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
