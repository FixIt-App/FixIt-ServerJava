import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Confirmation } from './confirmation.model';
import { ConfirmationService } from './confirmation.service';

@Component({
    selector: 'jhi-confirmation-detail',
    templateUrl: './confirmation-detail.component.html'
})
export class ConfirmationDetailComponent implements OnInit, OnDestroy {

    confirmation: Confirmation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private confirmationService: ConfirmationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInConfirmations();
    }

    load(id) {
        this.confirmationService.find(id).subscribe((confirmation) => {
            this.confirmation = confirmation;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInConfirmations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'confirmationListModification',
            (response) => this.load(this.confirmation.id)
        );
    }
}
