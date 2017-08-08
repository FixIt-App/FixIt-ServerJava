import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Worker } from './worker.model';
import { WorkerPopupService } from './worker-popup.service';
import { WorkerService } from './worker.service';
import { User, UserService } from '../../shared';
import { Work, WorkService } from '../work';
import { WorkType, WorkTypeService } from '../work-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-worker-dialog',
    templateUrl: './worker-dialog.component.html'
})
export class WorkerDialogComponent implements OnInit {

    worker: Worker;
    isSaving: boolean;

    users: User[];

    works: Work[];

    worktypes: WorkType[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private workerService: WorkerService,
        private userService: UserService,
        private workService: WorkService,
        private workTypeService: WorkTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.workService.query()
            .subscribe((res: ResponseWrapper) => { this.works = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.workTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.worktypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.worker.id !== undefined) {
            this.subscribeToSaveResponse(
                this.workerService.update(this.worker));
        } else {
            this.subscribeToSaveResponse(
                this.workerService.create(this.worker));
        }
    }

    private subscribeToSaveResponse(result: Observable<Worker>) {
        result.subscribe((res: Worker) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Worker) {
        this.eventManager.broadcast({ name: 'workerListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackWorkById(index: number, item: Work) {
        return item.id;
    }

    trackWorkTypeById(index: number, item: WorkType) {
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
    selector: 'jhi-worker-popup',
    template: ''
})
export class WorkerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private workerPopupService: WorkerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.workerPopupService
                    .open(WorkerDialogComponent as Component, params['id']);
            } else {
                this.workerPopupService
                    .open(WorkerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
