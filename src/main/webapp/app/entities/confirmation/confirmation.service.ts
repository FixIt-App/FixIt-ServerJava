import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Confirmation } from './confirmation.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ConfirmationService {

    private resourceUrl = 'api/confirmations';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(confirmation: Confirmation): Observable<Confirmation> {
        const copy = this.convert(confirmation);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(confirmation: Confirmation): Observable<Confirmation> {
        const copy = this.convert(confirmation);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Confirmation> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.expires = this.dateUtils
            .convertDateTimeFromServer(entity.expires);
    }

    private convert(confirmation: Confirmation): Confirmation {
        const copy: Confirmation = Object.assign({}, confirmation);

        copy.expires = this.dateUtils.toDate(confirmation.expires);
        return copy;
    }
}
