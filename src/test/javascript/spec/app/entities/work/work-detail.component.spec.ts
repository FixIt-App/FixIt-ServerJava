/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FixItTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WorkDetailComponent } from '../../../../../../main/webapp/app/entities/work/work-detail.component';
import { WorkService } from '../../../../../../main/webapp/app/entities/work/work.service';
import { Work } from '../../../../../../main/webapp/app/entities/work/work.model';

describe('Component Tests', () => {

    describe('Work Management Detail Component', () => {
        let comp: WorkDetailComponent;
        let fixture: ComponentFixture<WorkDetailComponent>;
        let service: WorkService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FixItTestModule],
                declarations: [WorkDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WorkService,
                    JhiEventManager
                ]
            }).overrideTemplate(WorkDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WorkDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Work(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.work).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
