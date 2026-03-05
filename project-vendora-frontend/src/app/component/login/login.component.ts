import {Component, OnInit} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {UserService} from '../../service/user.service';
import {UserAuthService} from '../../service/user-auth.service';
import {routes} from '../../app.routes';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  constructor(
    private userService: UserService,
    private userAuthservice: UserAuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
  }

  login(loginForm: NgForm){
    this.userService.login(loginForm.value).subscribe(
      (res:any) => {
        console.log(res);
        this.userAuthservice.setUser(res.user);
        this.userAuthservice.setToken(res.jwtToken);
        this.userAuthservice.setBusinesses(res.user.businessDTO)
        this.router.navigate(['/']);
      },
      error => console.log(error)
    );
  }

}
