/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FixItTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WorkTypeCategoryDetailComponent } from '../../../../../../main/webapp/app/entities/work-type-category/work-type-category-detail.component';
import { WorkTypeCategoryService } from '../../../../../../main/webapp/app/entities/work-type-category/work-type-category.service';
import { WorkTypeCategory } from '../../../../../../main/webapp/app/entities/work-type-category/work-type-category.model';

describe('Component Tests', () => {

    describe('WorkTypeCategory Management Detail Component', () => {
        let comp: WorkTypeCategoryDetailComponent;
        let fixture: ComponentFixture<WorkTypeCategoryDetailComponent>;
        let service: WorkTypeCategoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FixItTestModule],
                declarations: [WorkTypeCategoryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WorkTypeCategoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(WorkTypeCategoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WorkTypeCategoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkTypeCategoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new WorkTypeCategory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.workTypeCategory).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
