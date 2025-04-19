import { UserAccount } from '../entities/UserAccount';

export class LogoutController {
  static processLogout() {
    UserAccount.logout();
    return true;
  }
}