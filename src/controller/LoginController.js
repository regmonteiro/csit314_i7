import { UserAccount } from '../entities/UserAccount';

export class LoginController {
  static processLogin(email, password) {
    return UserAccount.login(email, password);
  }
}