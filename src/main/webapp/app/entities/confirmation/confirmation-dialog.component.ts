import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Confirmation } from './confirmation.model';
import { ConfirmationPopupService } from './confirmation-popup.service';
import { ConfirmationService } from './confirmation.service';
import { Customer, CustomerService } from '../customer';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-confirmation-dialog',
    templateUrl: './confirmation-dialog.component.html'
})
export class ConfirmationDialogComponent implements OnInit {

    confirmation: Confirmation;
    isSaving: boolean;

    customers: Customer[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private confirmationService: ConfirmationService,
        private customerService: CustomerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.customerService.query()
            .subscribe((res: ResponseWrapper) => { this.customers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.confirmation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.confirmationService.update(this.confirmation));
        } else {
            this.subscribeToSaveResponse(
                this.confirmationService.create(this.confirmation));
        }
    }

    private subscribeToSaveResponse(result: Observable<Confirmation>) {
        result.subscribe((res: Confirmation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Confirmation) {
        this.eventManager.broadcast({ name: 'confirmationListModification', content: 'OK'});
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

    trackCustomerById(index: number, item: Customer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-confirmation-popup',
    template: ''
})
export class ConfirmationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private confirmationPopupService: ConfirmationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.confirmationPopupService
                    .open(ConfirmationDialogComponent as Component, params['id']);
            } else {
                this.confirmationPopupService
                    .open(ConfirmationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
