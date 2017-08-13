/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FixItTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ConfirmationDetailComponent } from '../../../../../../main/webapp/app/entities/confirmation/confirmation-detail.component';
import { ConfirmationService } from '../../../../../../main/webapp/app/entities/confirmation/confirmation.service';
import { Confirmation } from '../../../../../../main/webapp/app/entities/confirmation/confirmation.model';

describe('Component Tests', () => {

    describe('Confirmation Management Detail Component', () => {
        let comp: ConfirmationDetailComponent;
        let fixture: ComponentFixture<ConfirmationDetailComponent>;
        let service: ConfirmationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FixItTestModule],
                declarations: [ConfirmationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ConfirmationService,
                    JhiEventManager
                ]
            }).overrideTemplate(ConfirmationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ConfirmationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfirmationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Confirmation(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.confirmation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
