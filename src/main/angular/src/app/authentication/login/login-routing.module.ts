import {RouterModule, Routes} from "@angular/router";
import {DoLoginComponent, LoginComponent} from "./components";
import {NgModule} from "@angular/core";

export const LoginRoutes: Routes = [
  {
    path: 'login',
    component: DoLoginComponent,
    children: [{path: '', component:LoginComponent}]

  }
];
@NgModule({
  imports: [RouterModule.forChild(LoginRoutes)],
  exports: [RouterModule]
})
export class LoginRoutingModule {
}
