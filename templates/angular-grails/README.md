Angular Grails
================================
[![Build Status](https://drone.io/github.com/craigburke/angular-grails/status.png)](https://drone.io/github.com/craigburke/angular-grails/latest)

This is an example app showcasing some of the ways that I use Angular within a Grails project. This is a proof of concept but it should serve as a nice starting point for anyone wanting to use the two frameworks together.

This project also makes use of [Twitter Bootstrap](http://getbootstrap.com/), [Angular UI Bootstrap](http://angular-ui.github.io/bootstrap/) and [Font Awesome](http://fortawesome.github.io/Font-Awesome/).

## Getting started
This project makes use of the [Grails Gradle plugin](https://github.com/grails/grails-gradle-plugin). You can start the application like this:
```bash
./gradlew run
```
You can run all the tests (Groovy and Javascript) like this:
```bash
./gradlew test
```

If you want to run just the Jasmine (JavaScript) tests use this command
```bash
./gradlew jasmineRun
```
The following will run the Jasmine tests in watch mode (so that tests are rerun when your source changes):
```bash
./gradlew jasmineWatch
```

## The Grails AngularController
This is a slightly modified version of the standard Grails RestfulController. It adds support for server side paging and can be used exactly the same way the RestfulController is used.

Here's how you would add a REST controller for the Book domain class:
```groovy
class BookController extends AngularController {
    BookController() {
        super(Book)
    }
}
```
## Grails Plugins

This project makes use of the Asset Pipeline along with two AngularJs specific asset pipeline plugins that I developed:

* [Angular Template Asset Pipeline](https://github.com/craigburke/angular-template-asset-pipeline)
* [Angular Annotate Asset Pipeline](https://github.com/craigburke/angular-annotate-asset-pipeline)

## The AngularJS angularGrails module
This project includes an AngularJS module called **angularGrails** that you can include as a dependency in your own angular modules.

### Services

#### CrudResourceFactory

This is a factory that you can use to create CrudResource object to help you make REST calls. 
This is essentially a wrapper for Angular's own **$resource** module but the methods from a CrudResource object return a promise from all its methods and supports paging.

Here's how you would create a CrudResource object
```javascript
function AuthorResource(CrudResourceFactory) {
    // set the rest url and resource name here
    return CrudResourceFactory('/api/author', 'Author');
}

angular.module('exampleApp.authors.services', ['angularGrails'])
    .factory('AuthorResource', AuthorResource);
```

Once you have a CrudResource object you can use it like this:

```javascript
AuthorResource.list({page: 1}).then(function(items) {
  this.items = items;
  // items also has a getTotalCount function that provides the total item count for paging
  this.totalCount = items.getTotalCount();
});

AuthorResource.create().then(function(item) {
  this.newItem = item;
});

AuthorResource.get(1).then(function(item) {
  this.currentItem = item;
});

var item = {id: 1, title: 'Foo Bar'};
AuthorResource.update(item);

AuthorResource.delete(1);
```
Each of the above functions can also accept an optional success and error callback function:

```javascript
var successFunction = function(response) {
    console.log("It worked!");
};

var errorFunction = function(response) {
    console.log("Uh oh!");
};

AuthorResource.delete(1, successFunction, errorFunction);
````
#### FlashService
Used in conjunction with the **flash-message** directive below. This service allows you to easily set different messages in your app. Each time a flash message is set it overrides the previous one.

```javascript
FlashService.success("Everything is fine");
FlashService.warning("Something bad is about to happen");
FlashService.error("Uh oh, something bad did happen");
FlashService.info("Something good or bad might happen");
FlashService.clear(); // Clear message
```

### Directives

#### crudButton

```javascript

```
The click actions of these buttons are automatically set to make the appropriate method call from the default CrudResource. For example, clicking the delete button will call the DefaultResource.delete method.


```html
<button crud-button="delete" item="ctrl.item" ></button>
<button crud-button="edit" item="ctrl.item" ></button>
<button crud-button="save" item="ctrl.item" ></button>
<button crud-button="create" ></button>
<button crud-button="cancel" ></button>
```

You can also include an optional **afterAction** parameter to register a callback or **isDisabled** to disable a button.

```html
<button crud-button="delete" item="ctrl.item" after-action="ctrl.logDelete()"></button>
<button crud-button="save" item="ctrl.item" is-disabled="form.$invalid"></button>
```

The button templates are located at:
`/grails-app/assets/templates/angular-grails/directives/buttons`

#### flashMessage
This directive is used along with the **FlashService** above to display messages on the page. 
```html
<div flash-message></div>
```

The flash message template is located at:
`/grails-app/assets/templates/angular-grails/directives/flash-message.tpl.html`

#### sortHeader / sortableColumn
This directive allows you to keep track of the current sort state of a table, and has an onSort callback to allow you to reload your data if need be.

```html
<thead sort-header ng-model="ctrl.sort" on-sort="ctrl.reloadData()">
    <th sortable-column title="Id" property="id"></th>
    <th sortable-column title="Name" property="name"></th>
</thead>
```

The sortable column template is located at:
`/grails-app/assets/templates/angular-grails/directives/sortable-column.tpl.html`

