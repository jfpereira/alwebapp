import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from "@angular/material/snack-bar";

import { Login } from '../../models';
import { LoginService } from "../../services";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private loginService: LoginService) { }

  ngOnInit(): void {
    this.formGenerator();
  }

  formGenerator() {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  login() {
    if(this.form.invalid) {
      this.snackBar.open("Dados Inválidos", "Error", { duration: 5000});
      return;
    }

    const login: Login = this.form.value;
    this.loginService.doLogin(login)
      .subscribe(
        data => {
          console.log(JSON.stringify(data));
          localStorage['token'] = data['data']['token'];
          const userData = JSON.parse(
            atob(data['data']['token'].split('.')[1]));
          if(userData['role'] == 'ROLE_ADMIN') {
            //this.router.navigate(['/admin'])
          } else {
            //this.router.navigate(['/manager'])
          }
        },
      err => {
          console.log(JSON.stringify(err));
          let msg: string = 'Try again';
          if(err['status'] == 401) {
            msg = 'Wrong Email'
          }
          this.snackBar.open(msg, "Error", {duration: 5000});
      }
  }

}
