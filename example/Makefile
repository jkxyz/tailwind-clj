styles_input=src/styles.css
styles_output_dev=target/dev/resources/public/styles.css
styles_output_prod=target/prod/resources/public/styles.css
tw_used_classes=target/prod/tailwind-used-classes

tw=yarn tailwindcss

.PHONY: dev
dev: $(styles_output_dev)

.PHONY: prod
prod: $(styles_output_prod)

.PHONY: clean
clean:
	-rm $(styles_output_dev) $(styles_output_prod) $(tw_used_classes)

$(styles_output_dev): $(styles_input) tailwind.config.js
	$(tw) build $(styles_input) -o $@

$(styles_output_prod): $(styles_input) tailwind.config.js $(tw_used_classes)
	NODE_ENV=production $(tw) build $(styles_input) -o $@

$(tw_used_classes): src/main/**/*.clj src/main/**/*.cljs
	clj -X:tailwind/used-classes :output-to $@
