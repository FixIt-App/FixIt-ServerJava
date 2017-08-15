import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Work } from './work.model';
import { WorkPopupService } from './work-popup.service';
import { WorkService } from './work.service';
import { Worker, WorkerService } from '../worker';
import { Customer, CustomerService } from '../customer';
import { Address, AddressService } from '../address';
import { WorkType, WorkTypeService } from '../work-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-work-dialog',
    templateUrl: './work-dialog.component.html'
})
export class WorkDialogComponent implements OnInit {

    work: Work;
    isSaving: boolean;

    workers: Worker[];

    customers: Customer[];

    addresses: Address[];

    worktypes: WorkType[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private workService: WorkService,
        private workerService: WorkerService,
        private customerService: CustomerService,
        private addressService: AddressService,
        private workTypeService: WorkTypeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.workerService.query()
            .subscribe((res: ResponseWrapper) => { this.workers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.customerService.query()
            .subscribe((res: ResponseWrapper) => { this.customers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.addressService.query()
            .subscribe((res: ResponseWrapper) => { this.addresses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.workTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.worktypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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

    trackWorkerById(index: number, item: Worker) {
        return item.id;
    }

    trackCustomerById(index: number, item: Customer) {
        return item.id;
    }

    trackAddressById(index: number, item: Address) {
        return item.id;
    }

    trackWorkTypeById(index: number, item: WorkType) {
        return item.id;
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
