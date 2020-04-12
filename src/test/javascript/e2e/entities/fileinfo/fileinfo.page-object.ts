import { element, by, ElementFinder } from 'protractor';

export class FileinfoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fileinfo div table .btn-danger'));
  title = element.all(by.css('jhi-fileinfo div h2#page-heading span')).first();

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class FileinfoUpdatePage {
  pageTitle = element(by.id('jhi-fileinfo-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  fileInput = element(by.id('file_file'));
  uploaddateInput = element(by.id('field_uploaddate'));
  notesInput = element(by.id('field_notes'));
  filetypeSelect = element(by.id('field_filetype'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setFileInput(file: string): Promise<void> {
    await this.fileInput.sendKeys(file);
  }

  async getFileInput(): Promise<string> {
    return await this.fileInput.getAttribute('value');
  }

  async setUploaddateInput(uploaddate: string): Promise<void> {
    await this.uploaddateInput.sendKeys(uploaddate);
  }

  async getUploaddateInput(): Promise<string> {
    return await this.uploaddateInput.getAttribute('value');
  }

  async setNotesInput(notes: string): Promise<void> {
    await this.notesInput.sendKeys(notes);
  }

  async getNotesInput(): Promise<string> {
    return await this.notesInput.getAttribute('value');
  }

  async setFiletypeSelect(filetype: string): Promise<void> {
    await this.filetypeSelect.sendKeys(filetype);
  }

  async getFiletypeSelect(): Promise<string> {
    return await this.filetypeSelect.element(by.css('option:checked')).getText();
  }

  async filetypeSelectLastOption(): Promise<void> {
    await this.filetypeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class FileinfoDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-fileinfo-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-fileinfo'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
