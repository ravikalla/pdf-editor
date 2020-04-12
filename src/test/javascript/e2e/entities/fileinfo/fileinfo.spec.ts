import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FileinfoComponentsPage, FileinfoDeleteDialog, FileinfoUpdatePage } from './fileinfo.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Fileinfo e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fileinfoComponentsPage: FileinfoComponentsPage;
  let fileinfoUpdatePage: FileinfoUpdatePage;
  let fileinfoDeleteDialog: FileinfoDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Fileinfos', async () => {
    await navBarPage.goToEntity('fileinfo');
    fileinfoComponentsPage = new FileinfoComponentsPage();
    await browser.wait(ec.visibilityOf(fileinfoComponentsPage.title), 5000);
    expect(await fileinfoComponentsPage.getTitle()).to.eq('Fileinfos');
  });

  it('should load create Fileinfo page', async () => {
    await fileinfoComponentsPage.clickOnCreateButton();
    fileinfoUpdatePage = new FileinfoUpdatePage();
    expect(await fileinfoUpdatePage.getPageTitle()).to.eq('Create or edit a Fileinfo');
    await fileinfoUpdatePage.cancel();
  });

  it('should create and save Fileinfos', async () => {
    const nbButtonsBeforeCreate = await fileinfoComponentsPage.countDeleteButtons();

    await fileinfoComponentsPage.clickOnCreateButton();
    await promise.all([
      fileinfoUpdatePage.setFileInput(absolutePath),
      fileinfoUpdatePage.setUploaddateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      fileinfoUpdatePage.setNotesInput('notes'),
      fileinfoUpdatePage.filetypeSelectLastOption()
    ]);
    expect(await fileinfoUpdatePage.getFileInput()).to.endsWith(fileNameToUpload, 'Expected File value to be end with ' + fileNameToUpload);
    expect(await fileinfoUpdatePage.getUploaddateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected uploaddate value to be equals to 2000-12-31'
    );
    expect(await fileinfoUpdatePage.getNotesInput()).to.eq('notes', 'Expected Notes value to be equals to notes');
    await fileinfoUpdatePage.save();
    expect(await fileinfoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fileinfoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Fileinfo', async () => {
    const nbButtonsBeforeDelete = await fileinfoComponentsPage.countDeleteButtons();
    await fileinfoComponentsPage.clickOnLastDeleteButton();

    fileinfoDeleteDialog = new FileinfoDeleteDialog();
    expect(await fileinfoDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Fileinfo?');
    await fileinfoDeleteDialog.clickOnConfirmButton();

    expect(await fileinfoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
