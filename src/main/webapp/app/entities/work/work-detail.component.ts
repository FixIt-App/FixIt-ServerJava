import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Work } from './work.model';
import { WorkService } from './work.service';

@Component({
    selector: 'jhi-work-detail',
    templateUrl: './work-detail.component.html'
})
export class WorkDetailComponent implements OnInit, OnDestroy {

    work: Work;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private workService: WorkService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWorks();
    }

    load(id) {
        this.workService.find(id).subscribe((work) => {
            this.work = work;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWorks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'workListModification',
            (response) => this.load(this.work.id)
        );
    }
}
