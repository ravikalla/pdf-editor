import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFileinfo, Fileinfo } from 'app/shared/model/fileinfo.model';
import { FileinfoService } from './fileinfo.service';
import { FileinfoComponent } from './fileinfo.component';
import { FileinfoDetailComponent } from './fileinfo-detail.component';
import { FileinfoUpdateComponent } from './fileinfo-update.component';

@Injectable({ providedIn: 'root' })
export class FileinfoResolve implements Resolve<IFileinfo> {
  constructor(private service: FileinfoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileinfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fileinfo: HttpResponse<Fileinfo>) => {
          if (fileinfo.body) {
            return of(fileinfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fileinfo());
  }
}

export const fileinfoRoute: Routes = [
  {
    path: '',
    component: FileinfoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Fileinfos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FileinfoDetailComponent,
    resolve: {
      fileinfo: FileinfoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Fileinfos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FileinfoUpdateComponent,
    resolve: {
      fileinfo: FileinfoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Fileinfos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FileinfoUpdateComponent,
    resolve: {
      fileinfo: FileinfoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Fileinfos'
    },
    canActivate: [UserRouteAccessService]
  }
];
