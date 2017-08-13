import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { WorkType } from './work-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WorkTypeService {

    private resourceUrl = 'api/work-types';

    constructor(private http: Http) { }

    create(workType: WorkType): Observable<WorkType> {
        const copy = this.convert(workType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(workType: WorkType): Observable<WorkType> {
        const copy = this.convert(workType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<WorkType> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(workType: WorkType): WorkType {
        const copy: WorkType = Object.assign({}, workType);
        return copy;
    }
}
