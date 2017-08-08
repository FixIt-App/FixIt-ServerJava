import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { WorkTypeCategory } from './work-type-category.model';
import { WorkTypeCategoryService } from './work-type-category.service';

@Component({
    selector: 'jhi-work-type-category-detail',
    templateUrl: './work-type-category-detail.component.html'
})
export class WorkTypeCategoryDetailComponent implements OnInit, OnDestroy {

    workTypeCategory: WorkTypeCategory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private workTypeCategoryService: WorkTypeCategoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWorkTypeCategories();
    }

    load(id) {
        this.workTypeCategoryService.find(id).subscribe((workTypeCategory) => {
            this.workTypeCategory = workTypeCategory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWorkTypeCategories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'workTypeCategoryListModification',
            (response) => this.load(this.workTypeCategory.id)
        );
    }
}
