import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Confirmation } from './confirmation.model';
import { ConfirmationService } from './confirmation.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-confirmation',
    templateUrl: './confirmation.component.html'
})
export class ConfirmationComponent implements OnInit, OnDestroy {
confirmations: Confirmation[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private confirmationService: ConfirmationService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.confirmationService.query().subscribe(
            (res: ResponseWrapper) => {
                this.confirmations = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInConfirmations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Confirmation) {
        return item.id;
    }
    registerChangeInConfirmations() {
        this.eventSubscriber = this.eventManager.subscribe('confirmationListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
