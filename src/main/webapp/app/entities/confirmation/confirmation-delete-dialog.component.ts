import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Confirmation } from './confirmation.model';
import { ConfirmationPopupService } from './confirmation-popup.service';
import { ConfirmationService } from './confirmation.service';

@Component({
    selector: 'jhi-confirmation-delete-dialog',
    templateUrl: './confirmation-delete-dialog.component.html'
})
export class ConfirmationDeleteDialogComponent {

    confirmation: Confirmation;

    constructor(
        private confirmationService: ConfirmationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.confirmationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'confirmationListModification',
                content: 'Deleted an confirmation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-confirmation-delete-popup',
    template: ''
})
export class ConfirmationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private confirmationPopupService: ConfirmationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.confirmationPopupService
                .open(ConfirmationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
