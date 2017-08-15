import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Worker } from './worker.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WorkerService {

    private resourceUrl = 'api/workers';

    constructor(private http: Http) { }

    create(worker: Worker): Observable<Worker> {
        const copy = this.convert(worker);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(worker: Worker): Observable<Worker> {
        const copy = this.convert(worker);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Worker> {
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

    private convert(worker: Worker): Worker {
        const copy: Worker = Object.assign({}, worker);
        return copy;
    }
}
